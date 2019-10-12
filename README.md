K8S Java DEMO 项目
============


#项目简介
   本项目是一个简单的 SpringBoot的WEB项目，用maven编译出jar包，可以直接用java命令执行后启动内置的Tomcat服务。主要用于DEMO Java 项目打出Docker镜像,上传至uhub，然后自动发布到UK8S的全流程。同时，项目中也配置了相应的日志收集以及性能监控所需要的配置文件。

##功能清单
1. 支持自动的CI/CD全流程
2. 演示自定义的JMX计数器，然后通过 JMX_Exportor暴露，支持Prometheus通过 Metrics查询
3. 演示通过 SideCar方式由FileBeat 向Logstash上报APP日志，这里我们用Tomcat Access Log演示

##环境简介
1. 一台UHost，安装Docker、JDK、Maven、GitLab runner、Jenkins  Slave。 其中GitLab Runner用户与K8S的Master作了ssh互信，可以直接在Gitlab-runner用户下通过 ssh k8s-master kubectl 命令对K8S进行执行。 Docker 中运行的包括
	a. Gitlab Docker
    b. Jenkins Docker    
2. 一个K8S集群，有default、preproduct、 product三个Namespace，分别模拟应于集成环境、预发布环境、生产环境
3. UHub，用于存放Docker镜像

##CI/CD流程简介
### GitLab Runner CI/CD
项目通过 GitLab Runner的来实现CI/CD，整体流程可以参考GitFlow。在Developer分支上完成开发同时自动部署到K8S的default namespace；在分Developer完成测试以后，提出Merge request；在项目负责人通过以后，打上Tag后自动升级到预发布环境；在预发布环境测试完成后，手工触发部署任务，将相应的版本部署到生产环境。
相应的实现可以参考.gitlab-ci.yaml文件

### Jenkins File
本项目也提供了一个Jenkins File用于演示如何在Jenkins Pipeline中增加相应的Stage来完成Docker镜像打包的过程。如果你仍然在用SVN或者无法迁移到GitLab Runner，则可以参考这个文件。

## 日志收集
### 应用日志收集
应用日志采用tomcat的access 日志来演示，主要流程是在Tomcat中生成accesslog，然后由filebeat发送到logstash进行分析以后再发给ElasticSearch进行最终处理。
架构上包括：
1. APP和Filebeat一起作为SideCar，运行在K8S里；
2. LogStash Server。在DMEO配置里是运行在UHOST；商用环境中看各自的情况
3. Elastic Search 使用用UES

### 日志生成相应的配置文件说明
1. DockerFile中的CMD一项，有--spring.config.location=/etc/appconfig/hello.properties 指定配置文件在容器中的路径为/etc/appconfig/hello.properties
2. 容器中的/etc/appconfig/hello.properties 为K8S的ConfigMap，配置文件为项目中的yaml/app-configmap.yml
3. 在hello.properties文件里配置了SpringBoot产生Tomcat 日志的方式，存贮目录为/accesslog
4. FileBeat的配置也是K8S的ConfigMap，配置文件为yaml/filebeat-config.yaml，在这个文件里配置了FileBeat的对应的LogStash的服务器地址 
5. hello_world_filebeat_template.yml是配置app和filebeat作为Sidecar运行的配置文件，包括了ConfigMap的实际 Mount点信息以及两个容器共用 emptydir{}作为配置
6. logstash_pipeline.conf是logstash的配置文件，这个文件里配置了UES的地址，放到LogStac应用的/etc/logstash/conf.d 目录下

### 系统日志收集
系统日志通过 DeamonSet方式收集，配置文件不包括在本项目中

## 应用监控
### Prometheus和Grafana安装
参考ucloud官网 UK8S文档的方式安装

### JVM和Tomcat监控相关的说明 
在项目中，我直接采用了JMX_Export的方式向Prometheus暴露监控数据，这样扩展性相对好，在增加要监控的应用数据时，只需要注册相应的MBean即可。
要注意的点是配置文件中相应的端口，JMX_Export是采用额外的端口对外提供Metrics，配置在DockerFile的CMD一行中。
JMX_Export的配置文件为   yaml/javaagent.yaml

### 自定义的JMX Bean 
项目中亦提供了相应的自定义JMX MBean，计数器采用了TimerTask的方式来随机增加。
相应的代码


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

以及同目录下对应的HelloworldMBean.java的代码。

