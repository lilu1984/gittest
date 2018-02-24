package com.wonders.tdsc.thirdpart.axis.test;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class TestTdscService {
    private void test1() throws Exception {
        String endpoint = "http://192.168.7.167:7002/fangdi/services/TdscService?wsdl";

        Service service = new Service();
        Call call = (Call) service.createCall();

        call.getMessageContext().setUsername("landaudit"); // �û�����
        call.getMessageContext().setPassword("landaudit"); // ����

        call.setTargetEndpointAddress(new java.net.URL(endpoint));
        call.setOperationName("receiptBlockInfo");

        String xml = "<?xml version='1.0' encoding='GB2312'?><landInfo><auditedNum>20080101-09</auditedNum>"
                + "<landName>����������·�ϲࡢ����·����ؿ�</landName>"
                + "<district>16</district>"
                + "<auditedDate>2008-01-24</auditedDate>"
                + "<layoutOrg>�������滮�����</layoutOrg>"
                + "<layoutDocNum>���أ�2007����224��225��162��227��226��</layoutDocNum>"
                + "<planOrg>����������ίԱ��</planOrg>"
                + "<planDocNum>�����2007����62��160��13��19��14��66��</planDocNum>"
                + "<storePlanDocNum>��������2007����632��509��</storePlanDocNum>"
                + "<landAuditedDoc>�õ����ģ�Base64��</landAuditedDoc>"
                + "<supplyCaseDoc>���ط�����Base64��</supplyCaseDoc>"
                + "<survey><surveyId>200818135583</surveyId><reportId>2007120305</reportId><landUse>221</landUse><constructArea>23896.7</constructArea></survey></landInfo>";

        Integer ret = (Integer) call.invoke(new Object[] { xml });
        System.out.println(ret);

    }

    private void test2() throws Exception {
        String endpoint = "http://192.168.7.167:7002/fangdi/services/TdscService?wsdl";
        String xml = "<?xml version='1.0' encoding='GB2312'?>"
                + "<contractInfo><auditedNum>test20080201�������ĺ�</auditedNum><contactNum>��ͬ���1</contactNum><contactDate>2008-01-31</contactDate></contractInfo>";

        Service service = new Service();
        Call call = (Call) service.createCall();

        call.getMessageContext().setUsername("landaudit"); // �û�����
        call.getMessageContext().setPassword("landaudit"); // ����

        call.setTargetEndpointAddress(new java.net.URL(endpoint));
        call.setOperationName("receiptContractInfo");

        Integer ret = (Integer) call.invoke(new Object[] { xml });
        System.out.println(ret);
    }

    public static void main(String[] args) throws Exception {
        new TestTdscService().test1();
    }
}