package com.wonders.tdsc.thirdpart.axis.test;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class TestTdscService {
    private void test1() throws Exception {
        String endpoint = "http://192.168.7.167:7002/fangdi/services/TdscService?wsdl";

        Service service = new Service();
        Call call = (Call) service.createCall();

        call.getMessageContext().setUsername("landaudit"); // 用户名。
        call.getMessageContext().setPassword("landaudit"); // 密码

        call.setTargetEndpointAddress(new java.net.URL(endpoint));
        call.setOperationName("receiptBlockInfo");

        String xml = "<?xml version='1.0' encoding='GB2312'?><landInfo><auditedNum>20080101-09</auditedNum>"
                + "<landName>奉贤区韩村路南侧、韩谊路西侧地块</landName>"
                + "<district>16</district>"
                + "<auditedDate>2008-01-24</auditedDate>"
                + "<layoutOrg>奉贤区规划管理局</layoutOrg>"
                + "<layoutDocNum>奉规地（2007）第224、225、162、227、226号</layoutDocNum>"
                + "<planOrg>奉贤区经济委员会</planOrg>"
                + "<planDocNum>奉经土（2007）第62、160、13、19、14、66号</planDocNum>"
                + "<storePlanDocNum>沪府土（2007）第632、509号</storePlanDocNum>"
                + "<landAuditedDoc>用地批文（Base64）</landAuditedDoc>"
                + "<supplyCaseDoc>供地方案（Base64）</supplyCaseDoc>"
                + "<survey><surveyId>200818135583</surveyId><reportId>2007120305</reportId><landUse>221</landUse><constructArea>23896.7</constructArea></survey></landInfo>";

        Integer ret = (Integer) call.invoke(new Object[] { xml });
        System.out.println(ret);

    }

    private void test2() throws Exception {
        String endpoint = "http://192.168.7.167:7002/fangdi/services/TdscService?wsdl";
        String xml = "<?xml version='1.0' encoding='GB2312'?>"
                + "<contractInfo><auditedNum>test20080201供地批文号</auditedNum><contactNum>合同编号1</contactNum><contactDate>2008-01-31</contactDate></contractInfo>";

        Service service = new Service();
        Call call = (Call) service.createCall();

        call.getMessageContext().setUsername("landaudit"); // 用户名。
        call.getMessageContext().setPassword("landaudit"); // 密码

        call.setTargetEndpointAddress(new java.net.URL(endpoint));
        call.setOperationName("receiptContractInfo");

        Integer ret = (Integer) call.invoke(new Object[] { xml });
        System.out.println(ret);
    }

    public static void main(String[] args) throws Exception {
        new TestTdscService().test1();
    }
}