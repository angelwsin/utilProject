package jms.test;

import java.util.Properties;

import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;

public class JMSTest {
	
	
	public static void main(String[] args) throws Exception{
		Properties env = new Properties();
		  env.setProperty(Context.SECURITY_PRINCIPAL, "system");
		  env.setProperty(Context.SECURITY_CREDENTIALS, "manager");
		  env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		  env.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
		  env.setProperty("connectionFactoryNames", "chatFactory");
		  env.setProperty("topic.topic1", "jms.topic1");
		  InitialContext ctx  = new InitialContext(env);
		TopicConnectionFactory factory = (TopicConnectionFactory) ctx.lookup("chatFactory");
		System.out.println(factory==null);
		TopicConnection topicConnection=   factory.createTopicConnection();
		System.out.println(topicConnection.getMetaData().getJMSXPropertyNames().nextElement().toString());
	}

}
