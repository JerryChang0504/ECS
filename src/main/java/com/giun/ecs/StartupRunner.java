package com.giun.ecs;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements ApplicationRunner {

	@Autowired
	private DataSource dataSource;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try (Connection conn = dataSource.getConnection()) {
			System.out.println("資料庫連線成功：" + conn.getMetaData().getURL());
		} catch (SQLException e) {
			System.err.println("資料庫連線失敗：" + e.getMessage());
		}
	}

}
