package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.constants.Constant;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * 罗健
 * 2021/4/30
 */
@Controller
public class ActivityController {

    @Autowired
    ActivityService activityService;
    @Autowired
    UserService userService;
    @Autowired
    ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String activityIndex(Model model) {
        List<User> userList = userService.queryAllUsers();
        model.addAttribute("userList", userList);
        return "/workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/queryActivityForPageByCondition.do")
    @ResponseBody
    public Object queryActivityForPageByCondition(int pageNo, int pageSize, String name, String owner, String startDate, String endDate) {
        Map<String, Object> map = new HashMap<>();
        int beginNo = (pageNo - 1) * pageSize;
        map.put("beginNo", beginNo);
        map.put("pageSize", pageSize);
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);


        //获取所有的市场活动
        List<Activity> activityList = activityService.queryActivityForPageByCondition(map);
        //获取市场活动条数
        long totalRows = activityService.queryCountOfActivityByCondition(map);

        Map<String, Object> retMap = new HashMap<>();

        retMap.put("activityList", activityList);
        retMap.put("totalRows", totalRows);

        return retMap;

    }

    @RequestMapping("workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public ReturnObject saveCreateActivity(Activity activity, HttpSession session) {
        ReturnObject returnObject = new ReturnObject();
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        int ret = 0;

        activity.setId(UUIDUtils.getUUID());
        activity.setCreateBy(user.getId());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));

        try {
            ret = activityService.saveActivity(activity);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ret > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("添加市场活动失败");
            }
            return returnObject;
        }
    }

    @RequestMapping("workbench/activity/editActivity.do")
    @ResponseBody
    public Activity editActivity(String id) {
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }

    @RequestMapping("workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity, HttpSession session) {
        ReturnObject returnObject = new ReturnObject();
        int ret = 0;

        User user = (User) session.getAttribute(Constant.SESSION_USER);
        activity.setEditBy(user.getId());
        activity.setEditTime(DateUtils.formatDateTime(new Date()));

        try {
            ret = activityService.updateActivity(activity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ret > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("更新市场活动失败");
            }

            return returnObject;
        }
    }

    @RequestMapping("workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id) {
        ReturnObject returnObject = new ReturnObject();
        int ret = 0;

        try {
            ret = activityService.deleteActivityByIds(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ret > 0) {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("删除市场活动失败");
            }
            return returnObject;
        }
    }

    @RequestMapping("workbench/activity/exportAllActivity.do")
    public void exportAllActivity(HttpServletResponse response) throws IOException {
        List<Activity> activityList = activityService.queryAllActivityForDetail();

        //工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        //工作表
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        //行,从0开始
        HSSFRow row = sheet.createRow(0);
        //单元格
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("开销");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建日期");
        cell = row.createCell(8);
        cell.setCellValue("创建人");
        cell = row.createCell(9);
        cell.setCellValue("编辑日期");
        cell = row.createCell(10);
        cell.setCellValue("编辑人");

        //单元格样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        //导入市场活动信息
        Activity activity = null;
        for (int i = 0; i < activityList.size(); i++) {
            activity = activityList.get(i);

            row = sheet.createRow(i + 1);

            cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getId());
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getOwner());
            cell = row.createCell(2);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getName());
            cell = row.createCell(3);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getStartDate());
            cell = row.createCell(4);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getEndDate());
            cell = row.createCell(5);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getCost());
            cell = row.createCell(6);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getDescription());
            cell = row.createCell(7);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getCreateTime());
            cell = row.createCell(8);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getCreateBy());
            cell = row.createCell(9);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getEditTime());
            cell = row.createCell(10);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getEditBy());
        }

        //下载表格
        //设置响应类型为字节流
        response.setContentType("application/octet-stream;charset=UTF-8");

        //设置响应头信息，让浏览器接收到响应信息后，打开下载窗口，以附件形式下载文件
        String fileName = URLEncoder.encode("市场活动表", "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");

        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
        wb.close();

    }

    @RequestMapping("workbench/activity/exportActivitySelective.do")
    public void exportActivitySelective(String[] id, HttpServletResponse response) throws IOException {
        List<Activity> activityList = activityService.queryActivityForDetailByIds(id);

        //工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        //工作表
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        //行,从0开始
        HSSFRow row = sheet.createRow(0);
        //单元格
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("开销");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建日期");
        cell = row.createCell(8);
        cell.setCellValue("创建人");
        cell = row.createCell(9);
        cell.setCellValue("编辑日期");
        cell = row.createCell(10);
        cell.setCellValue("编辑人");

        //单元格样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        //导入市场活动信息
        Activity activity = null;
        for (int i = 0; i < activityList.size(); i++) {
            activity = activityList.get(i);

            row = sheet.createRow(i + 1);

            cell = row.createCell(0);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getId());
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getOwner());
            cell = row.createCell(2);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getName());
            cell = row.createCell(3);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getStartDate());
            cell = row.createCell(4);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getEndDate());
            cell = row.createCell(5);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getCost());
            cell = row.createCell(6);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getDescription());
            cell = row.createCell(7);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getCreateTime());
            cell = row.createCell(8);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getCreateBy());
            cell = row.createCell(9);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getEditTime());
            cell = row.createCell(10);
            cell.setCellStyle(style);
            cell.setCellValue(activity.getEditBy());
        }

        //下载表格
        //设置响应类型为字节流
        response.setContentType("application/octet-stream;charset=UTF-8");

        //设置响应头，使浏览器以附件形式下载文件
        String fileName = URLEncoder.encode("市场活动表节选", "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");

        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
        wb.close();
    }

    @RequestMapping("workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile, HttpSession session) {
        User user = (User) session.getAttribute(Constant.SESSION_USER);
        Map<String, Object> retMap = new HashMap<>();

        try {
            //将每行数据放到市场活动集合中，进行批量导入
            List<Activity> activityList = new ArrayList<>();
            //获得输入流
            InputStream is = activityFile.getInputStream();
            //工作簿
            HSSFWorkbook wb = new HSSFWorkbook(is);
            //工作表
            HSSFSheet sheet = wb.getSheetAt(0);

            HSSFRow row = null;
            HSSFCell cell = null;

            Activity activity = null;

            //行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);//从第二行开始，第一行是标题
                activity = new Activity();
                //补全市场活动缺失的信息
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formatDateTime(new Date()));
                activity.setCreateBy(user.getId());

                //列
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    String cellValue = getCellValue(cell);

                    if(j==0){
                        activity.setName(cellValue);
                    }else if(j==1){
                        activity.setStartDate(cellValue);
                    }else if(j==2){
                        activity.setEndDate(cellValue);
                    }else if(j==3){
                        activity.setCost(cellValue);
                    }else if(j==4){
                        activity.setDescription(cellValue);
                    }
                }
                activityList.add(activity);

            }
            //批量添加到数据库
            int ret = activityService.saveActivityByList(activityList);
            retMap.put("code", Constant.RETURN_OBJECT_CODE_SUCCESS);
            retMap.put("count", ret);
        } catch (IOException e) {
            e.printStackTrace();
            retMap.put("code", Constant.RETURN_OBJECT_CODE_FAIL);
            retMap.put("message", "导入市场活动失败");
        }

        return retMap;
    }

    //根据不同的单元格数据类型，读取为string类型
    private static String getCellValue(HSSFCell cell) {
        String ret = "";

        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                ret = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                ret = cell.getBooleanCellValue() + "";
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                ret = cell.getNumericCellValue() + "";
                break;
            default:
                ret = "";
        }
        return ret;
    }

    @RequestMapping("workbench/activity/detailActivity.do")
    public String toDetailActivity(String id,Model model){
        Activity activity=activityService.queryActivityForDetailById(id);
        List<ActivityRemark> remarkList=activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
        //把数据保存到request中
        model.addAttribute("activity",activity);
        model.addAttribute("remarkList",remarkList);
        //请求转发
        return "workbench/activity/detail";
    }

}
