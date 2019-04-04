package ${package_name}.service.impl;

import ${package_name}.entity.${table_name};
import ${package_name}.mapper.${table_name}Mapper;
import ${package_name}.mapper.Z${table_name}Mapper;
import ${package_name}.service.${table_name}Service;
import java.util.ArrayList;
import ${dtoImportName}.*;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author ${author}
*/
@Service
public class ${table_name}ServiceImpl implements ${table_name}Service {

    @Resource
    private ${table_name}Mapper ${table_name?uncap_first}mapper;

    @Resource
    private Z${table_name}Mapper Z${table_name?uncap_first}mapper;

    @Override
    public int delete(Delete${table_name}Dto dto) {
        //  dto需要调用对应的BeanUtils来转换为可处理的对象
        //  return ${table_name?uncap_first}mapper.deleteByExample(bean);
        return 0;
    }

    @Override
    public int deleteById(Integer id){
        //  return ${table_name?uncap_first}mapper.deleteByPrimaryKey(id);
        return 0;
    }

    @Override
    public int insert(Insert${table_name}Dto dto){
        //  dto需要调用对应的BeanUtils来转换为可处理的对象
        //  return ${table_name?uncap_first}mapper.insert(bean);
        return 0;
    }

    @Override
    public List<${table_name}> selectList(Select${table_name}Dto dto) {
        //  dto需要调用对应的BeanUtils来转换为可处理的对象
        List<${table_name}> list = new ArrayList<${table_name}>();
        //  return ${table_name?uncap_first}mapper.selectByExample(bean);
        return list;
    }


    @Override
    public List<${table_name}> select${table_name}PageList(Select${table_name}PageListDto dto) {
        //  dto需要调用对应的BeanUtils来转换为可处理的对象
        //  分页插件的调用
        List<${table_name}> list = new ArrayList<${table_name}>();
        //  return ${table_name?uncap_first}mapper.selectByExample(bean);
        return list;
    }

    @Override
    public ${table_name} selectById(Integer id) {
        ${table_name} t = new ${table_name}();
        //  return ${table_name?uncap_first}mapper.selectByPrimaryKey(id);
        return t;
    }

    @Override
    public int update(Update${table_name}Dto dto) {
        //  dto需要调用对应的BeanUtils来转换为可处理的对象
        //  return ${table_name?uncap_first}mapper.updateByPrimaryKey(bean);
        return 0;
    }

}