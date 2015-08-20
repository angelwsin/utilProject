package com.jms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;

public class Chat implements MessageListener{
	private TopicConnection topicConnection;
	private TopicSession  topicSession;
	private TopicPublisher topicPublisher;
	private String  username;
	  public Chat(String topicFactory,String topicName,String username)throws Exception {
		// TODO Auto-generated constructor stub
		  //jndi
		  Properties env = new Properties();
		  env.setProperty(Context.SECURITY_PRINCIPAL, "system");
		  env.setProperty(Context.SECURITY_CREDENTIALS, "manager");
		  env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		  env.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
		  env.setProperty("connectionFactoryNames", "chatFactory");
		  env.setProperty("topic.topic1", "jms.topic1");
		  InitialContext ctx  = new InitialContext(env);
		TopicConnectionFactory factory = (TopicConnectionFactory) ctx.lookup(topicFactory);
		topicConnection=   factory.createTopicConnection();
		TopicSession pubSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		TopicSession subSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		 Topic topic =   (Topic) ctx.lookup(topicName);
		 topicPublisher = pubSession.createPublisher(topic);
		//null表示不过滤 true表示不接受自己发送的
		 TopicSubscriber subscriber = subSession.createSubscriber(topic, null, true);
		 subscriber.setMessageListener(this);
		 this.username = username;
		 this.topicSession = pubSession;
		 topicConnection.start();
		 
		 
	}

	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			TextMessage textMessage = (TextMessage) message;
			System.out.println(textMessage.getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
    protected void writerMessage(String text) throws JMSException{
		// TODO Auto-generated method stub
		TextMessage  msg = topicSession.createTextMessage();
        msg.setText(username+":"+text);
		topicPublisher.publish(msg);
	}
    
    public void close()throws JMSException{
    	topicConnection.close();
    }
    public static void main(String[] args)throws Exception {
    	  Chat chat = new Chat("chatFactory", "topic1", "lisi");
		  BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		    String  text = reader.readLine();
		   while(true){
			   if("exit".equals(text)){
				    chat.close();
				    System.exit(1);
			   }else {
			    chat.writerMessage(text);
			}
			   text = reader.readLine(); 
		   }
	}

}
