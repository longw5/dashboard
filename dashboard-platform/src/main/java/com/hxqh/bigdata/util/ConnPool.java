package com.hxqh.bigdata.util;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.logging.Logger;
import javax.sql.DataSource;
import com.hxqh.bigdata.common.Constants;
import com.hxqh.bigdata.conf.ConfigurationManager;

/**
 * 简单实现数据库连接池
 * 采用代理模式
 * @author wl
 *
 */
public class ConnPool implements DataSource {

    //使用LinkedList集合存放数据库连接
    private static LinkedList<Connection> connPool = new LinkedList<Connection>();
    
	private static final String driver = ConfigurationManager.getProperty(Constants.JDBC_DRIVER);
	private static final String url = ConfigurationManager.getProperty(Constants.JDBC_URL);
	private static final String username = ConfigurationManager.getProperty(Constants.JDBC_USER);
	private static final String password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD);
	private static final int InitSize = 50;

    //在静态代码块中加载配置文件
    static{
        try {
            //加载驱动
            Class.forName(driver);
            for(int i = 0; i < InitSize; i++){
                Connection conn = DriverManager.getConnection(url, username, password);
                //将创建的连接添加的list中
                System.out.println("初始化数据库连接池，创建第 " + (i + 1) +" 个连接，添加到池中");
                connPool.add(conn);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取数据库连接
     */
    public Connection getConnection() throws SQLException {
        if(connPool.size() > 0){
            //从集合中获取一个连接
            final Connection conn = connPool.removeFirst();
            //返回Connection的代理对象
            return (Connection) Proxy.newProxyInstance(ConnPool.class.getClassLoader(), conn.getClass().getInterfaces(), new InvocationHandler() {
                public Object invoke(Object proxy, Method method, Object[] args)
                        throws Throwable {
                    if(!"close".equals(method.getName())){
                        return method.invoke(conn, args);
                    }else{
                        connPool.add(conn);
                        System.out.println("关闭连接，实际还给了连接池");
                        System.out.println("池中连接数为 " + connPool.size());
                        return null;
                    }
                }
            });
        }else{
            throw new RuntimeException("数据库繁忙，稍后再试");
        }
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    public void setLoginTimeout(int seconds) throws SQLException {

    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public Object unwrap(Class iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class iface) throws SQLException {
        return false;
    }

    public Connection getConnection(String username, String password)
            throws SQLException {
        return null;
    }

}
