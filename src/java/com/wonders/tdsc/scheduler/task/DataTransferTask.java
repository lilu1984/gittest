package com.wonders.tdsc.scheduler.task;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.wonders.esframework.dic.util.DicDataUtil;
import com.wonders.esframework.scheduler.timer.SchedulerTask;
import com.wonders.tdsc.common.GlobalConstants;

public class DataTransferTask extends SchedulerTask {
    /** 日志 */
    private Logger logger = Logger.getLogger(getClass());

    public void run() {
        Connection conn = null;
        CallableStatement proc = null;

        try {
            String transId = "" + getJsonObject().get("transId");
            logger.debug("transId:" + transId);

            HashMap taskMap = (HashMap) DicDataUtil.getInstance().getComplexDicItemMap(GlobalConstants.DIC_DATA_TRANSFER_TASK, transId);
            logger.debug("taskMap:" + taskMap);

            // 1、获得数据库连接
            long start = System.currentTimeMillis();
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection((String) taskMap.get("DB_URL"), (String) taskMap.get("DB_NAME"), (String) taskMap.get("DB_PASSWORD"));
            conn.setAutoCommit(false);
            long end = System.currentTimeMillis();
            logger.debug("=========获得数据库连接，共花费" + (end - start) + "毫秒============");

            // 2、调用存储过程
            String procedureName = "{ call " + taskMap.get("PROCEDURE_NAME") + "(?,?) }";

            logger.debug("==============执行存储过程->" + procedureName + "-开始...");
            proc = conn.prepareCall(procedureName);
            // 设置参数
            proc.setString(1, transId);
            proc.setLong(2, System.currentTimeMillis());
            proc.execute();
            logger.debug("==============执行存储过程->" + procedureName + "-结束！");
        } catch (JSONException e) {
            logger.error(e);
        } catch (ClassNotFoundException e) {
            logger.error(e);
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            try {
                if (proc != null)
                    proc.close();

                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }

    }
}
