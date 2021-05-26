package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.constants.Constant;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.domain.ValueData;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicTypeService;
import com.bjpowernode.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 罗健
 * 2021/4/28
 */
@Controller
public class DicValueController {

    @Autowired
    DicValueService dicValueService;
    @Autowired
    DicTypeService dicTypeService;

    @RequestMapping("/settings/dictionary/value/index.do")
    public String index(Model model) {
        List<DicValue> dicValueList = dicValueService.selectAllDicValue();
        model.addAttribute("dicValueList", dicValueList);
        return "/settings/dictionary/value/index";
    }

    @RequestMapping("/settings/dictionary/value/toSave.do")
    public String toSave(Model model) {
        List<DicType> dicTypeList = dicTypeService.queryAllDicType();
        model.addAttribute("dicTypeList", dicTypeList);
        return "/settings/dictionary/value/save";
    }

    @RequestMapping("settings/dictionary/value/saveCreateDicValue.do")
    @ResponseBody
    public ReturnObject saveCreateDicValue(DicValue dicValue) {
        ReturnObject returnObject = new ReturnObject();
        int num = 0;
        try {
            dicValue.setId(UUIDUtils.getUUID());
            num = dicValueService.addDicValue(dicValue);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (num > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("增加字典值失败");

            }
            return returnObject;
        }

    }

    @RequestMapping("/settings/dictionary/value/editDicValue.do")
    public String editDicValue(String id, Model model) {
        DicValue dicValue = dicValueService.selectDicValue(id);
        model.addAttribute("dicValue", dicValue);
        return "/settings/dictionary/value/edit";
    }

    @RequestMapping("settings/dictionary/value/saveEditDicValue.do")
    @ResponseBody
    public ReturnObject saveEditDicValue(DicValue dicValue) {
        ReturnObject returnObject = new ReturnObject();
        int num = 0;

        try {
            num = dicValueService.updateDicValueSelective(dicValue);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (num > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("更改字典值失败");

            }
            return returnObject;
        }
    }

    @RequestMapping("settings/dictionary/value/deleteDicValueByIds.do")
    @ResponseBody
    public ReturnObject deleteDicValueByIds(String[] id) {
        ReturnObject returnObject = new ReturnObject();
        int num = 0;

        try {
            num = dicValueService.deleteDicValue(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (num > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("删除字典值失败");

            }
            return returnObject;
        }
    }

    @RequestMapping("settings/dictionary/value/queryDicValueForPage.do")
    @ResponseBody
    public Object queryDicValueForPage(int pageNo,int pageSize){
        Map<String,Object> map = new HashMap<>();
        int beginNo = (pageNo - 1)*pageSize;
        map.put("beginNo", beginNo);
        map.put("pageSize", pageSize);

        ValueData valueData = new ValueData();

        List<DicValue> dicValueList = dicValueService.selectDicValueForPage(map);
        long totalRows = dicValueService.selectCountOfDicValueForPage();

        valueData.setDicValueList(dicValueList);
        valueData.setTotalRows(totalRows);


        return valueData;
    }
}
