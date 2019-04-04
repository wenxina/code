package ${package_name}.service;

import ${package_name}.entity.${table_name};
import ${dtoImportName}.*;
import java.util.List;
/**
* @author ${author}
*/
public interface ${table_name}Service{

    int  delete(Delete${table_name}Dto dto);

    int deleteById(Integer id);

    int insert(Insert${table_name}Dto dto);

    List<${table_name}> selectList(Select${table_name}Dto dto);

    List<${table_name}> select${table_name}PageList(Select${table_name}PageListDto dto);

    /**
    * 描述：根据Id获取DTO
    * @param id
    */
    ${table_name} selectById(Integer id);

    int update(Update${table_name}Dto dto);

}