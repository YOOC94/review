package cn.spark.study.project.domain;

import java.io.Serializable;

/**
 * 页面切片转化率
 * @author Administrator
 *
 */
public class PageSplitConvertRate implements Serializable {

	private long taskid;
	private String convertRate;
	
	public long getTaskid() {
		return taskid;
	}
	public void setTaskid(long taskid) {
		this.taskid = taskid;
	}
	public String getConvertRate() {
		return convertRate;
	}
	public void setConvertRate(String convertRate) {
		this.convertRate = convertRate;
	}
	
}
