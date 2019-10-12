K8S Java DEMO ��Ŀ
============


#��Ŀ���
   ����Ŀ��һ���򵥵� SpringBoot��WEB��Ŀ����maven�����jar��������ֱ����java����ִ�к��������õ�Tomcat������Ҫ����DEMO Java ��Ŀ���Docker����,�ϴ���uhub��Ȼ���Զ�������UK8S��ȫ���̡�ͬʱ����Ŀ��Ҳ��������Ӧ����־�ռ��Լ����ܼ������Ҫ�������ļ���

##�����嵥
1. ֧���Զ���CI/CDȫ����
2. ��ʾ�Զ����JMX��������Ȼ��ͨ�� JMX_Exportor��¶��֧��Prometheusͨ�� Metrics��ѯ
3. ��ʾͨ�� SideCar��ʽ��FileBeat ��Logstash�ϱ�APP��־������������Tomcat Access Log��ʾ

##�������
1. һ̨UHost����װDocker��JDK��Maven��GitLab runner��Jenkins  Slave�� ����GitLab Runner�û���K8S��Master����ssh���ţ�����ֱ����Gitlab-runner�û���ͨ�� ssh k8s-master kubectl �����K8S����ִ�С� Docker �����еİ���
	a. Gitlab Docker
    b. Jenkins Docker    
2. һ��K8S��Ⱥ����default��preproduct�� product����Namespace���ֱ�ģ��Ӧ�ڼ��ɻ�����Ԥ������������������
3. UHub�����ڴ��Docker����

##CI/CD���̼��
### GitLab Runner CI/CD
��Ŀͨ�� GitLab Runner����ʵ��CI/CD���������̿��Բο�GitFlow����Developer��֧����ɿ���ͬʱ�Զ�����K8S��default namespace���ڷ�Developer��ɲ����Ժ����Merge request������Ŀ������ͨ���Ժ󣬴���Tag���Զ�������Ԥ������������Ԥ��������������ɺ��ֹ������������񣬽���Ӧ�İ汾��������������
��Ӧ��ʵ�ֿ��Բο�.gitlab-ci.yaml�ļ�

### Jenkins File
����ĿҲ�ṩ��һ��Jenkins File������ʾ�����Jenkins Pipeline��������Ӧ��Stage�����Docker�������Ĺ��̡��������Ȼ����SVN�����޷�Ǩ�Ƶ�GitLab Runner������Բο�����ļ���

## ��־�ռ�
### Ӧ����־�ռ�
Ӧ����־����tomcat��access ��־����ʾ����Ҫ��������Tomcat������accesslog��Ȼ����filebeat���͵�logstash���з����Ժ��ٷ���ElasticSearch�������մ���
�ܹ��ϰ�����
1. APP��Filebeatһ����ΪSideCar��������K8S�
2. LogStash Server����DMEO��������������UHOST�����û����п����Ե����
3. Elastic Search ʹ����UES

### ��־������Ӧ�������ļ�˵��
1. DockerFile�е�CMDһ���--spring.config.location=/etc/appconfig/hello.properties ָ�������ļ��������е�·��Ϊ/etc/appconfig/hello.properties
2. �����е�/etc/appconfig/hello.properties ΪK8S��ConfigMap�������ļ�Ϊ��Ŀ�е�yaml/app-configmap.yml
3. ��hello.properties�ļ���������SpringBoot����Tomcat ��־�ķ�ʽ������Ŀ¼Ϊ/accesslog
4. FileBeat������Ҳ��K8S��ConfigMap�������ļ�Ϊyaml/filebeat-config.yaml��������ļ���������FileBeat�Ķ�Ӧ��LogStash�ķ�������ַ 
5. hello_world_filebeat_template.yml������app��filebeat��ΪSidecar���е������ļ���������ConfigMap��ʵ�� Mount����Ϣ�Լ������������� emptydir{}��Ϊ����
6. logstash_pipeline.conf��logstash�������ļ�������ļ���������UES�ĵ�ַ���ŵ�LogStacӦ�õ�/etc/logstash/conf.d Ŀ¼��

### ϵͳ��־�ռ�
ϵͳ��־ͨ�� DeamonSet��ʽ�ռ��������ļ��������ڱ���Ŀ��

## Ӧ�ü��
### Prometheus��Grafana��װ
�ο�ucloud���� UK8S�ĵ��ķ�ʽ��װ

### JVM��Tomcat�����ص�˵�� 
����Ŀ�У���ֱ�Ӳ�����JMX_Export�ķ�ʽ��Prometheus��¶������ݣ�������չ����Ժã�������Ҫ��ص�Ӧ������ʱ��ֻ��Ҫע����Ӧ��MBean���ɡ�
Ҫע��ĵ��������ļ�����Ӧ�Ķ˿ڣ�JMX_Export�ǲ��ö���Ķ˿ڶ����ṩMetrics��������DockerFile��CMDһ���С�
JMX_Export�������ļ�Ϊ   yaml/javaagent.yaml

### �Զ����JMX Bean 
��Ŀ�����ṩ����Ӧ���Զ���JMX MBean��������������TimerTask�ķ�ʽ��������ӡ�
��Ӧ�Ĵ���


``` java
	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
		List< MBeanServer > servers = MBeanServerFactory.findMBeanServer(null);
		System.out.println("Found MBean Servers : "+ servers);



		for (MBeanServer server:servers){
			try {
				server.registerMBean( jmx_bean , new ObjectName("hello_bean:name=helloworld" ));
			} catch (InstanceAlreadyExistsException e) {
				e.printStackTrace();
			} catch (MBeanRegistrationException e) {
				e.printStackTrace();
			} catch (NotCompliantMBeanException e) {
				e.printStackTrace();
			} catch (MalformedObjectNameException e) {
				e.printStackTrace();
			}
		}
```

�Լ�ͬĿ¼�¶�Ӧ��HelloworldMBean.java�Ĵ��롣

