package com.incon.common.sms.utils;

import freemarker.template.Template;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * @author wang
 */
public class GeneratorUtil {

    private Properties properties = new Properties();
    private ResultSet resultSet;
    private ResultSet resultSet2;
    private PreparedStatement stmt;
    private PreparedStatement stmt2;

    private static String GENERATORPATH;

    {
        try {

            /*InputStream inStream = GeneratorUtil.class.getResourceAsStream("/generatorConfig.properties");*/

            BufferedReader bufferedReader = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:generatorConfig.properties")));

            /*BufferedReader bufferedReader = new BufferedReader(new FileReader(url));*/

            properties.load(bufferedReader);
            GENERATORPATH = properties.getProperty("GENERATORPATH");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String[] tableName = properties.getProperty("tableName").split(",");
    String changeTableName = "";

    public Connection getConnection() throws Exception {
        Class.forName(properties.getProperty("DRIVER"));
        Connection connection = DriverManager.getConnection(properties.getProperty("URL"), properties.getProperty("USER"), properties.getProperty("PASSWORD"));
        return connection;
    }

    public static void main(String[] args) {
        GeneratorUtil codeGenerateUtils = new GeneratorUtil();
        codeGenerateUtils.makeDir();
        codeGenerateUtils.generate();

        List<String> warnings = new ArrayList<>();
        boolean overwrite = false;
        Configuration config = null;
        try {
            //指定逆向工程配置文件
            /*InputStream inputStream = GeneratorUtil.class.getResourceAsStream("/generatorConfig.xml");*/
            BufferedReader bufferedReader = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:generatorConfig.xml")));
            //File configFile = new File(new File("").getCanonicalPath() + "/src/main/resources/generatorConfig.xml");
            ConfigurationParser cp = new ConfigurationParser(warnings);
            config = cp.parseConfiguration(bufferedReader);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                    callback, warnings);
            myBatisGenerator.generate(null);
        } catch (Exception e) {
            System.out.println("自动构建出错");
            e.printStackTrace();
        }
        System.out.println("自动构建成功,代码位置在:" + GENERATORPATH);
    }

    public String replaceUnderLineAndUpperCase(String str) {
        StringBuffer sb = new StringBuffer();
        sb.append(str);
        int count = sb.indexOf("_");
        while (count != 0) {
            int num = sb.indexOf("_", count);
            count = num + 1;
            if (num != -1) {
                char ss = sb.charAt(count);
                char ia = (char) (ss - 32);
                sb.replace(count, count + 1, ia + "");
            }
        }
        String result = sb.toString().replaceAll("_", "");
        return StringUtils.capitalize(result);
    }

    /**
     * 生成目录结构
     */
    public void makeDir() {
        String str = properties.getProperty("GENERATORPATH") + "\\src\\main\\java\\com\\incon\\common\\" + properties.getProperty("yourpackage") + "\\";
        String[] strs = {"controller", "service\\impl", "entity", "mapper", "biz", "dao\\impl", "utils", "config", "enums"};
        dirPath(str, strs);

        String strd = properties.getProperty("GENERATORPATH") + "\\src\\main\\java\\com\\incon\\common\\interfaces\\dto\\" + properties.getProperty("yourpackage");
        File files = new File(strd);
        if (!files.exists()) {
            files.mkdirs();
        }

        String strsd = properties.getProperty("GENERATORPATH") + "\\src\\main\\resources\\";
        String[] strss = {"mapper\\mapper", "mapper\\zmapper"};
        dirPath(strsd, strss);
    }

    /**
     * 路径位置
     *
     * @param str  父级路径
     * @param strs 待生成路径数组
     */
    private void dirPath(String str, String[] strs) {
        for (String stry : strs) {
            String path = str + stry;
            File files = new File(path);
            if (!files.exists()) {
                files.mkdirs();
            }
        }
    }

    /**
     * 根据模板生成代码
     */
    private void generate() {
        try {
            Connection connection = getConnection();
            if (!connection.isClosed()) {
                System.out.println("Succeeded connecting to the Database!");
            } else {
                System.err.println("connect filed");
            }
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            for (int i = 0; i < tableName.length; i++) {
                List<String> columnComments = new ArrayList<>();
                /*changeTableName = replaceUnderLineAndUpperCase(tableName[i]);*/
                /*表名不做处理*/
                changeTableName = tableName[i];
                resultSet = databaseMetaData.getColumns(null, "%", tableName[i], "%");
                if (i < 1) {
                    // 生成BeanUtils工具类
                    generateUtilsFile();
                }
                //生成服务层接口文件
                generateServiceInterfaceFile();
                //生成服务实现层文件
                generateServiceImplFile();
                //生成Controller层文件
                generateControllerFile();
                //生成Mapper文件
                generateMapperFile();
                //生成ZMapper文件
                generateZMapperFile();
                String sql = "select * from " + tableName[i];
                stmt = connection.prepareStatement(sql);
                resultSet = stmt.executeQuery(sql);
                stmt2 = connection.prepareStatement(sql);
                /**
                 * mysql
                 * */
                /*resultSet2 = stmt2.executeQuery("show full columns from " + tableName[i]);
                while (resultSet2.next()) {
                    columnComments.add(resultSet2.getString("Comment"));
                }*/
                /**
                 * oracle
                 * */
                resultSet2 = stmt2.executeQuery("select * from user_tab_columns WHERE table_name = '" + tableName[i] + "'");
                while (resultSet2.next()) {
                    columnComments.add(resultSet2.getString("COLUMN_NAME"));
                }
                ResultSetMetaData data = resultSet.getMetaData();
                StringBuffer contentStr = new StringBuffer();
                StringBuffer contentStr1 = new StringBuffer();
                StringBuffer contentStr2 = new StringBuffer();
                StringBuffer contentStr3 = new StringBuffer();
                StringBuffer contentStr4 = new StringBuffer();
                StringBuffer contentStr5 = new StringBuffer();

                contentStr5.append("package " + properties.getProperty("packageName") + properties.getProperty("yourpackage") + ".utils;\n\n");
                contentStr5.append("public class " + changeTableName + "Utils{\n\n");
                contentStr5.append("}");
                File file5 = new File(properties.getProperty("GENERATORPATH") + "\\src\\main\\java\\com\\incon\\common\\" + properties.getProperty("yourpackage") + "\\utils\\" + changeTableName + "Utils.java");
                FileWriter fw5 = new FileWriter(file5, true);
                fw5.write(contentStr5.toString());
                fw5.flush();
                fw5.close();

                contentStr.append("package com.incon.common.interfaces.dto.");
                contentStr.append(properties.getProperty("yourpackage"));
                contentStr.append(";\n\n");
                contentStr.append("import lombok.Data;\nimport io.swagger.annotations.ApiModelProperty;\nimport com.incon.common.interfaces.dto.BaseDto;\n\n\n");
                contentStr.append("@Data\n");
                contentStr.append("public class ");
                contentStr.append("Delete" + changeTableName + "Dto extends BaseDto {\n\n");
                for (int s = 1; s <= data.getColumnCount(); s++) {
                    String[] str = data.getColumnName(s).split("_");
                    String names = "";
                    for (int x = 0; x < str.length; x++) {
                        if (x > 0) {
                            names += str[x].substring(0, 1).toUpperCase() + str[x].substring(1);
                        } else {
                            names = str[x];
                        }
                    }
                    if (data.getColumnTypeName(s).equals("INT") || data.getColumnTypeName(s).equals("SMALLINT")) {
                        contentStr.append("  @ApiModelProperty(" + '"' + columnComments.get(s - 1) + '"' + ")\n");
                        contentStr.append("  private Integer " + names + "; \n\n");
                    } else if (data.getColumnTypeName(s).equals("LONG")) {
                        contentStr.append("  @ApiModelProperty(" + '"' + columnComments.get(s - 1) + '"' + ")\n");
                        contentStr.append("  private Long " + names + "; \n\n");
                    } else {
                        contentStr.append("  @ApiModelProperty(" + '"' + columnComments.get(s - 1) + '"' + ")\n");
                        contentStr.append("  private String " + names + "; \n\n");
                    }
                }
                contentStr.append("}");
                File file = new File(properties.getProperty("GENERATORPATH") + "\\src\\main\\java\\com\\incon\\common\\interfaces\\dto\\" + properties.getProperty("yourpackage") + "\\Delete" + changeTableName + "Dto.java");
                FileWriter fw = new FileWriter(file, true);
                fw.write(contentStr.toString());
                fw.flush();
                fw.close();

                contentStr1.append("package com.incon.common.interfaces.dto.");
                contentStr1.append(properties.getProperty("yourpackage"));
                contentStr1.append(";\n\n");
                contentStr1.append("import lombok.Data;\nimport io.swagger.annotations.ApiModelProperty;\nimport com.incon.common.interfaces.dto.BaseDto;\n\n\n");
                contentStr1.append("@Data\n");
                contentStr1.append("public class ");
                contentStr1.append("Insert" + changeTableName + "Dto extends BaseDto {\n\n");
                for (int s = 1; s <= data.getColumnCount(); s++) {
                    String[] str = data.getColumnName(s).split("_");
                    String names = "";
                    for (int x = 0; x < str.length; x++) {
                        if (x > 0) {
                            names += str[x].substring(0, 1).toUpperCase() + str[x].substring(1);
                        } else {
                            names = str[x];
                        }
                    }
                    if (data.getColumnTypeName(s).equals("INT") || data.getColumnTypeName(s).equals("SMALLINT")) {
                        contentStr1.append("  @ApiModelProperty(" + '"' + columnComments.get(s - 1) + '"' + ")\n");
                        contentStr1.append("  private Integer " + names + ";\n\n");
                    } else if (data.getColumnTypeName(s).equals("LONG")) {
                        contentStr1.append("  @ApiModelProperty(" + '"' + columnComments.get(s - 1) + '"' + ")\n");
                        contentStr1.append("  private Long " + names + "; \n\n");
                    } else {
                        contentStr1.append("  @ApiModelProperty(" + '"' + columnComments.get(s - 1) + '"' + ")\n");
                        contentStr1.append("  private String " + names + "; \n\n");
                    }
                }
                contentStr1.append("}");
                File file1 = new File(properties.getProperty("GENERATORPATH") + "\\src\\main\\java\\com\\incon\\common\\interfaces\\dto\\" + properties.getProperty("yourpackage") + "\\Insert" + changeTableName + "Dto.java");
                FileWriter fw1 = new FileWriter(file1, true);
                fw1.write(contentStr1.toString());
                fw1.flush();
                fw1.close();

                contentStr2.append("package com.incon.common.interfaces.dto.");
                contentStr2.append(properties.getProperty("yourpackage"));
                contentStr2.append(";\n\n");
                contentStr2.append("import lombok.Data;\nimport io.swagger.annotations.ApiModelProperty;\nimport com.incon.common.interfaces.dto.BaseDto;\n\n\n");
                contentStr2.append("@Data\n");
                contentStr2.append("public class ");
                contentStr2.append("Select" + changeTableName + "Dto extends BaseDto {\n\n");
                for (int s = 1; s <= data.getColumnCount(); s++) {
                    String[] str = data.getColumnName(s).split("_");
                    String names = "";
                    for (int x = 0; x < str.length; x++) {
                        if (x > 0) {
                            names += str[x].substring(0, 1).toUpperCase() + str[x].substring(1);
                        } else {
                            names = str[x];
                        }
                    }
                    if (data.getColumnTypeName(s).equals("INT") || data.getColumnTypeName(s).equals("SMALLINT")) {
                        contentStr2.append("  @ApiModelProperty(" + '"' + columnComments.get(s - 1) + '"' + ")\n");
                        contentStr2.append("  private Integer " + names + ";\n\n");
                    } else if (data.getColumnTypeName(s).equals("LONG")) {
                        contentStr2.append("  @ApiModelProperty(" + '"' + columnComments.get(s - 1) + '"' + ")\n");
                        contentStr2.append("  private Long " + names + ";\n\n");
                    } else {
                        contentStr2.append("  @ApiModelProperty(" + '"' + columnComments.get(s - 1) + '"' + ")\n");
                        contentStr2.append("  private String " + names + ";\n\n");
                    }
                }
                contentStr2.append("}");
                File file2 = new File(properties.getProperty("GENERATORPATH") + "\\src\\main\\java\\com\\incon\\common\\interfaces\\dto\\" + properties.getProperty("yourpackage") + "\\Select" + changeTableName + "Dto.java");
                FileWriter fw2 = new FileWriter(file2, true);
                fw2.write(contentStr2.toString());
                fw2.flush();
                fw2.close();

                contentStr3.append("package com.incon.common.interfaces.dto.");
                contentStr3.append(properties.getProperty("yourpackage"));
                contentStr3.append(";\n\n");
                contentStr3.append("import lombok.Data;\nimport io.swagger.annotations.ApiModelProperty;\nimport com.incon.common.interfaces.dto.PageDto;\n\n\n");
                contentStr3.append("@Data\n");
                contentStr3.append("public class ");
                contentStr3.append("Select" + changeTableName + "PageListDto extends PageDto {\n\n");
                for (int s = 1; s <= data.getColumnCount(); s++) {
                    String[] str = data.getColumnName(s).split("_");
                    String names = "";
                    for (int x = 0; x < str.length; x++) {
                        if (x > 0) {
                            names += str[x].substring(0, 1).toUpperCase() + str[x].substring(1);
                        } else {
                            names = str[x];
                        }
                    }
                    if (data.getColumnTypeName(s).equals("INT") || data.getColumnTypeName(s).equals("SMALLINT")) {
                        contentStr3.append("  @ApiModelProperty(" + '"' + columnComments.get(s - 1) + '"' + ")\n");
                        contentStr3.append("  private Integer " + names + ";\n\n");
                    } else if (data.getColumnTypeName(s).equals("LONG")) {
                        contentStr3.append("  @ApiModelProperty(" + '"' + columnComments.get(s - 1) + '"' + ")\n");
                        contentStr3.append("  private Long " + names + ";\n\n");
                    } else {
                        contentStr3.append("  @ApiModelProperty(" + '"' + columnComments.get(s - 1) + '"' + ")\n");
                        contentStr3.append("  private String " + names + ";\n\n");
                    }
                }
                contentStr3.append("}");
                File file3 = new File(properties.getProperty("GENERATORPATH") + "\\src\\main\\java\\com\\incon\\common\\interfaces\\dto\\" + properties.getProperty("yourpackage") + "\\Select" + changeTableName + "PageListDto.java");
                FileWriter fw3 = new FileWriter(file3, true);
                fw3.write(contentStr3.toString());
                fw3.flush();
                fw3.close();


                contentStr4.append("package com.incon.common.interfaces.dto.");
                contentStr4.append(properties.getProperty("yourpackage"));
                contentStr4.append(";\n\n");
                contentStr4.append("import lombok.Data;\nimport io.swagger.annotations.ApiModelProperty;\nimport com.incon.common.interfaces.dto.BaseDto;\n\n\n");
                contentStr4.append("@Data\n");
                contentStr4.append("public class ");
                contentStr4.append("Update" + changeTableName + "Dto extends BaseDto {\n\n");
                for (int s = 1; s <= data.getColumnCount(); s++) {
                    String[] str = data.getColumnName(s).split("_");
                    String names = "";
                    for (int x = 0; x < str.length; x++) {
                        if (x > 0) {
                            names += str[x].substring(0, 1).toUpperCase() + str[x].substring(1);
                        } else {
                            names = str[x];
                        }
                    }
                    if (data.getColumnTypeName(s).equals("INT") || data.getColumnTypeName(s).equals("SMALLINT")) {
                        contentStr4.append("  @ApiModelProperty(" + '"' + columnComments.get(s - 1) + '"' + ")\n");
                        contentStr4.append("  private Integer " + names + ";\n\n");
                    } else if (data.getColumnTypeName(s).equals("LONG")) {
                        contentStr4.append("  @ApiModelProperty(" + '"' + columnComments.get(s - 1) + '"' + ")\n");
                        contentStr4.append("  private Long " + names + ";\n\n");
                    } else {
                        contentStr4.append("  @ApiModelProperty(" + '"' + columnComments.get(s - 1) + '"' + ")\n");
                        contentStr4.append("  private String " + names + ";\n\n");
                    }
                }
                contentStr4.append("}");
                File file4 = new File(properties.getProperty("GENERATORPATH") + "\\src\\main\\java\\com\\incon\\common\\interfaces\\dto\\" + properties.getProperty("yourpackage") + "\\Update" + changeTableName + "Dto.java");
                FileWriter fw4 = new FileWriter(file4, true);
                fw4.write(contentStr4.toString());
                fw4.flush();
                fw4.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

        }
    }

    private void generateUtilsFile() throws Exception {
        final String suffix = ".java";
        final String path = properties.getProperty("GENERATORPATH") + "\\src\\main\\java\\com\\incon\\common\\" + properties.getProperty("yourpackage") + "\\utils\\BeanUtils" + suffix;
        final String templateName = "BeanUtils.ftl";
        File mapperFile = new File(path);
        Map<String, Object> dataMap = new HashMap<>();
        generateFileByTemplate(templateName, mapperFile, dataMap);
    }

    private void generateServiceImplFile() throws Exception {
        final String suffix = "ServiceImpl.java";
        final String path = properties.getProperty("GENERATORPATH") + "\\src\\main\\java\\com\\incon\\common\\" + properties.getProperty("yourpackage") + "\\service\\impl\\" + changeTableName + suffix;
        final String templateName = "ServiceInterface.ftl";
        File mapperFile = new File(path);
        Map<String, Object> dataMap = new HashMap<>();
        generateFileByTemplate(templateName, mapperFile, dataMap);
    }

    private void generateServiceInterfaceFile() throws Exception {
        final String suffix = "Service.java";
        final String path = properties.getProperty("GENERATORPATH") + "\\src\\main\\java\\com\\incon\\common\\" + properties.getProperty("yourpackage") + "\\service\\" + changeTableName + suffix;
        final String templateName = "Interface.ftl";
        File mapperFile = new File(path);
        Map<String, Object> dataMap = new HashMap<>();
        generateFileByTemplate(templateName, mapperFile, dataMap);
    }

    private void generateControllerFile() throws Exception {
        final String suffix = "Controller.java";
        final String path = properties.getProperty("GENERATORPATH") + "\\src\\main\\java\\com\\incon\\common\\" + properties.getProperty("yourpackage") + "\\controller\\" + changeTableName + suffix;
        final String templateName = "Controller.ftl";
        File mapperFile = new File(path);
        Map<String, Object> dataMap = new HashMap<>();
        generateFileByTemplate(templateName, mapperFile, dataMap);
    }

    private void generateMapperFile() throws Exception {
        final String suffix = "Mapper.xml";
        final String path = properties.getProperty("GENERATORPATH") + "\\src\\main\\resources\\mapper\\zmapper\\" + "Z" + changeTableName + suffix;
        final String templateName = "Mapper.ftl";
        File mapperFile = new File(path);
        Map<String, Object> dataMap = new HashMap<>();
        generateFileByTemplate(templateName, mapperFile, dataMap);

    }

    private void generateZMapperFile() throws Exception {
        final String suffix = "Mapper.java";
        final String path = properties.getProperty("GENERATORPATH") + "\\src\\main\\java\\com\\incon\\common\\" + properties.getProperty("yourpackage") + "\\mapper\\" + "Z" + changeTableName + suffix;
        final String templateName = "ZMapper.ftl";
        File mapperFile = new File(path);
        Map<String, Object> dataMap = new HashMap<>();
        generateFileByTemplate(templateName, mapperFile, dataMap);

    }

    private void generateFileByTemplate(final String templateName, File file, Map<String, Object> dataMap) throws Exception {
        Template template = FreeMarkerTemplateUtils.getTemplate(templateName);
        FileOutputStream fos = new FileOutputStream(file);
        dataMap.put("table_name_small", properties.getProperty("tableName"));
        dataMap.put("table_name", changeTableName);
        dataMap.put("table_nameEx", changeTableName + "Example");
        dataMap.put("author", properties.getProperty("AUTHOR"));
        dataMap.put("dtoImportName", properties.getProperty("dtoImportName") + properties.getProperty("yourpackage"));
        dataMap.put("package_name", properties.getProperty("packageName") + properties.getProperty("yourpackage"));
        dataMap.put("serverName", properties.getProperty("yourpackage"));
        Writer out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"), 10240);
        template.process(dataMap, out);
    }
}
