/**
 * 一个适用于pad应用开发的页面结构框架
 * 包含三层页面
 * 底页面（包含一级菜单），二级菜单，详细页
 * 主要关注处理的内容有
 * 1.fragment使用（动画，传值，调用方法等）
 * 2.动画部分
 * 3.页面响应事件的传递，因为是三层页面，点击上层页面控件，事件的传递需要注意
 * */
package cn.zytec.lee.app;


import android.app.Application;


/** 
* @ClassName: App 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 香格李拉   limingdl@yeah.net
* @date 2013-5-13 上午09:06:36  
*/
public class App extends Application {
	//获取屏幕的宽和高
	public static int displayWidth;
	public static int displayHeight;
	
//	public final static int SECONDMENUWIDTH = 40;
//	public final static int DETAILWIDTH = 40;
//	public final static int EXPANDDETAILWIDTH = 80;
	/**
	 * 1.动画总结
	 * 2.界面显示终端适配总结
	 * 3.本地数据库总结
	 * 4.页面事件响应传递机制总结
	 * 5.图片处理总结**/
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	
}
