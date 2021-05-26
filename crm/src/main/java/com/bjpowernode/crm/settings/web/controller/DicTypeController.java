package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.constants.Constant;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.service.DicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 罗健
 * 2021/4/27
 */
@Controller
public class DicTypeController {
    @Autowired
    DicTypeService dicTypeService;

    @RequestMapping("/settings/dictionary/type/index.do")
    public String dicTypeIndex(Model model){
        List<DicType> dicTypeList = dicTypeService.queryAllDicType();
        model.addAttribute("dicTypeList", dicTypeList);
        return "/settings/dictionary/type/index";
    }

    @RequestMapping("/settings/dictionary/type/toSave.do")
    public String dicTypeSave(){
        return "settings/dictionary/type/save";
    }

    @RequestMapping("/settings/dictionary/type/checkCode.do")
    @ResponseBody
    public ReturnObject checkCode(String code){
        ReturnObject returnObject = new ReturnObject();
        DicType dicType = dicTypeService.queryDicTypeByCode(code);

        if(dicType==null){
            //编码为新编码，不重复
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("编码已存在");
        }

        return returnObject;
    }

    @RequestMapping("/settings/dictionary/type/saveDicType.do")
    @ResponseBody
    public ReturnObject saveDicType(DicType dicType){
        ReturnObject returnObject = new ReturnObject();
        int num = 0;
        try {
            num = dicTypeService.insertDicType(dicType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("insert into tbl_dic_type failed");
        }
        finally {

            if(num>0){
                //保存成功
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("保存失败");
            }


            return returnObject;
        }

    }

    @RequestMapping("/settings/dictionary/type/toedit.do")
    public String toEdit(String code,Model model){
        DicType dicType = dicTypeService.queryDicTypeByCode(code);
        model.addAttribute("dicType", dicType);
        return "/settings/dictionary/type/edit";
    }

    @RequestMapping("/settings/dictionary/type/saveEditDicType.do")
    @ResponseBody
    public ReturnObject saveEditDicType(DicType dicType){
        ReturnObject returnObject = new ReturnObject();
        int num = 0;
        try {
            num = dicTypeService.updateDicType(dicType);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(num > 0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("更新失败");
            }
            return returnObject;
        }
    }

    @RequestMapping("/settings/dictionary/type/deleteDicTypeByCodes.do")
    @ResponseBody
    public ReturnObject deleteDicTypeByCodes(String[] code){
        ReturnObject returnObject = new ReturnObject();
        int num = 0;
        try {
            num = dicTypeService.deleteDicType(code);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(num > 0){
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("删除失败");
            }
            return returnObject;
        }
    }
}
