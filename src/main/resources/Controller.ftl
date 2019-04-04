package ${package_name}.controller;

import ${package_name}.service.${table_name}Service;
import ${package_name}.entity.${table_name};
import ${dtoImportName}.*;
import com.core.common.springboot.util.ResponseData;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import io.swagger.annotations.ApiOperation;


/**
* @author ${author}
*/

@RestController
@RequestMapping("/${serverName}/${table_name?uncap_first}")
public class ${table_name}Controller {

    @Autowired
    private ${table_name}Service ${table_name?uncap_first}service;


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("根据对象删除")
    public ResponseData delete(@RequestBody Delete${table_name}Dto dto){
        int count = ${table_name?uncap_first}service.delete(dto);
        if(count > 0 ){
            return ResponseData.ok(count);
        }else{
            return ResponseData.status(500, "删除失败",null);
        }
    }



    @RequestMapping(value = "/deleteById", method = RequestMethod.GET)
    @ApiOperation("根据Id删除")
    public ResponseData deleteById(@RequestParam (value = "id")Integer id){
        int count = ${table_name?uncap_first}service.deleteById(id);
        if(count > 0 ){
            return ResponseData.ok(count);
        }else{
            return ResponseData.status(500, "删除失败",null);
        }
    }


    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ApiOperation("新增接口")
    public ResponseData insert(@RequestBody  Insert${table_name}Dto dto) {
        int count = ${table_name?uncap_first}service.insert(dto);
        if(count > 0 ){
            return ResponseData.ok(count);
        }else{
            return ResponseData.status(500, "新增失败",null);
        }
    }

    @RequestMapping(value = "/selectList", method = RequestMethod.POST)
    @ApiOperation("查询接口")
    public ResponseData selectList(@RequestBody  Select${table_name}Dto dto){
        List<${table_name}> ${table_name?uncap_first}  = ${table_name?uncap_first}service.selectList(dto);
        if(${table_name?uncap_first}.size() > 0 ){
            return ResponseData.ok(${table_name?uncap_first});
        }else{
            return ResponseData.status(500, "查询失败",null);
        }
    }

    @RequestMapping(value = "/select${table_name}PageList", method = RequestMethod.POST)
    @ApiOperation("分页查询")
    public ResponseData select${table_name}PageList(@RequestBody Select${table_name}PageListDto dto) {
    List<${table_name}> ${table_name?uncap_first}  = ${table_name?uncap_first}service.select${table_name}PageList(dto);
        if(${table_name?uncap_first}.size() > 0 ){
            return ResponseData.ok(${table_name?uncap_first});
        }else{
            return ResponseData.status(500, "查询失败",null);
        }
    }


    @RequestMapping(value = "/selectById", method = RequestMethod.GET)
    @ApiOperation("根据Id查询")
    public ResponseData selectById(@RequestParam (value = "id")Integer id){
    ${table_name} ${table_name?uncap_first}  = ${table_name?uncap_first}service.selectById(id);
        if(${table_name?uncap_first} != null){
            return ResponseData.ok(${table_name?uncap_first});
        }else{
            return ResponseData.status(500, "查询失败",null);
        }
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation("修改接口")
    public ResponseData update(@RequestBody Update${table_name}Dto dto){
        int count  = ${table_name?uncap_first}service.update(dto);
        if(count > 0){
            return ResponseData.ok(count);
        }else{
            return ResponseData.status(500, "修改失败",null);
        }
    }
}