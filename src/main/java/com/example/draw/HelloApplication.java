package com.example.draw;
/*2.0
直线平移，选中->解决
选中变拖动图标->解决
多次双击问题->解决
椭圆画法->解决一半
删除->解决
点画法->放弃
button一直执行问题
* */
/*3.0
多次双击BUG修复->解决
内部显示面积周长
* */
/*4.0
按钮一直使用
椭圆闪烁问题->解决
* */
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.IOException;
import java.util.ArrayList;
class WhileButton extends Button {
    private ExecuteTimer timer = new ExecuteTimer(this);

    public WhileButton(String s) {
        setText(s);
        this.addEventFilter(MouseEvent.ANY, event -> {
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                timer.start();
            } else {
                timer.stop();
            }
        });
    }

    class ExecuteTimer extends AnimationTimer {
        private long lastUpdate = 0L;
        private Button mbtn;

        public ExecuteTimer(Button button) {
            this.mbtn = button;
        }

        @Override
        public void handle(long now) {
            if (this.lastUpdate > 100) {
                if (mbtn.isPressed()) {
                    mbtn.fire();
                }
            }
            this.lastUpdate = now;
        }
    }
}

public class HelloApplication extends Application {
    private  static double pressedx=0,pressedy=0,dertax=0,dertay=0,dertax2=0,dertay2=0;//解决直线的拖动问题
    static ArrayList<Shape> data=new ArrayList<Shape>();//存储所有图形的数组
    @Override
    public void start(Stage stage) throws IOException {//舞台，场景，主组容器，主菜单
        Group group = new Group();//主组容器
        final Label reporter = new Label();//显示鼠标位置标签
        reporter.setLayoutX(1020);
        reporter.setLayoutY(960);
        Scene scene = new Scene(group, 1200, 1000);
//        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());//添加外部CSS失败
//        Application.setUserAgentStylesheet(getClass().getResource("style.css").toExternalForm());

        //定义一个label，获取shape的属性，然后位置和centerx，centery一样
        Group boardgroup=new Group();//容纳几何图形组

        VBox box=new VBox();//容纳头部两个菜单
        CreateMneu(boardgroup,box,scene);//创建主菜单
        createMonitoredLabel(reporter, scene);//右下角显示坐标
        group.getChildren().addAll(box,reporter,boardgroup);//把菜单，坐标，画板主体放进Group
        stage.setTitle("DrawBoard");
        stage.setScene(scene);
        stage.show();

    }//舞台，场景，主组容器，主菜单
    private void Update(Group g1,HBox box){//每次都把所有shape放到group里，
        g1.getChildren().clear();
        for(int i=0;i<data.size();i++){
            g1.getChildren().add(data.get(i));
            jianting2(g1,data.get(i),box);
        }
    }//每次都把所有shape包括更新过的放到group里，如果要实现文件保存可以直接邪恶一个方法保存data即可
    private void CreateMneu(Group boardgroup,VBox box,Scene scene){//创建主菜单
        WhileButton circlebutton = new WhileButton("圆形");circlebutton.setStyle("-fx-font-size:20");
        circlebutton.setId("menubutton");
        Button rectanglebutton = new Button("矩形");rectanglebutton.setStyle("-fx-font-size:20");
        rectanglebutton.setId("menubutton");
        Button tuoyuanbutton = new Button("椭圆");tuoyuanbutton.setStyle("-fx-font-size:20");
        tuoyuanbutton.setId("menubutton");
        Button linebutton = new Button("直线");linebutton.setStyle("-fx-font-size:20");
        linebutton.setId("menubutton");
        Button dotbutton = new Button("清空");dotbutton.setStyle("-fx-font-size:20");
        dotbutton.setId("menubutton");
        HBox menubox = new HBox();
        menubox.setMinWidth(1200);
        menubox.setMinHeight(60);
        menubox.setStyle("-fx-background-color:rgb(220,220,220)");
        menubox.setAlignment(Pos.CENTER);
//        menubox.setLayoutX(500);menubox.setLayoutY(20);
        menubox.getChildren().addAll(circlebutton,rectanglebutton,tuoyuanbutton,linebutton,dotbutton);

        HBox xuanbox = new HBox(30);//放置二级菜单
        box.getChildren().addAll(menubox,xuanbox);
        box.setStyle("-fx-background-color:rgb(220,220,220)");
        box.setMinHeight(90);
        box.setAlignment(Pos.CENTER);
        Update(boardgroup,xuanbox);
        box.setOnMouseEntered(event -> {//设置鼠标进出样式
            if (event.getEventType()==MouseEvent.MOUSE_ENTERED){
                box.setCursor(Cursor.HAND);
            }
        });
        box.setOnMouseExited(event -> {//设置鼠标进出样式
            if (event.getEventType()==MouseEvent.MOUSE_EXITED){
                box.setCursor(Cursor.DEFAULT);
            }
        });
        circlebutton.setOnAction(e->{
            Circle c=new Circle() ;//定义一个圆
            CreateSonMenu(boardgroup,xuanbox,c);//设置属性
            Drawshape(c,scene);//画圆
            c.setFill(Color.GREENYELLOW);//默认颜色
            data.add(c);//加入数组
            Update(boardgroup,xuanbox);//更新group组

        });
        rectanglebutton.setOnAction(e->{
            Rectangle r=new Rectangle();
            CreateSonMenu(boardgroup,xuanbox,r);
            Drawshape(r,scene);
            r.setFill(Color.rgb(26,125,207));
            data.add(r);
            Update(boardgroup,xuanbox);
        });
        linebutton.setOnAction(e->{
            Line l=new Line();
            CreateSonMenu(boardgroup,xuanbox,l);
            Drawshape(l,scene);
            l.setFill(Color.rgb(0,245,255));
            data.add(l);
            Update(boardgroup,xuanbox);
        });
        tuoyuanbutton.setOnAction(e->{
            Ellipse ee=new Ellipse();
            CreateSonMenu(boardgroup,xuanbox,ee);
            Drawshape(ee,scene);
            ee.setFill(Color.rgb(220,220,220));
            data.add(ee);
            Update(boardgroup,xuanbox);
        });
        dotbutton.setOnAction(e->{
            xuanbox.getChildren().clear();
            data.clear();
            boardgroup.getChildren().clear();
        });
    }//创建主菜单
    private void Drawshape(Shape ashape,Scene scene){//画图，并保存
         ashape.setStrokeWidth(3);//为了方便观察，把初始边框设置为了3
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {//记录按下坐标
                if(event.getEventType()==MouseEvent.MOUSE_PRESSED){
                    if (ashape instanceof Circle){
                        ((Circle) ashape).setCenterX(event.getSceneX());
                        ((Circle) ashape).setCenterY(event.getSceneY());
                    } else if (ashape instanceof Rectangle) {
                        ((Rectangle) ashape).setX(event.getSceneX());
                        ((Rectangle) ashape).setY(event.getSceneY());
                    }else if (ashape instanceof Line){
                        ((Line) ashape).setStartX(event.getSceneX());
                        ((Line) ashape).setStartY(event.getSceneY());
                        ((Line) ashape).setEndX(event.getSceneX());
                        ((Line) ashape).setEndY(event.getSceneY());
                    } else if (ashape instanceof Ellipse) {
                        ((Ellipse) ashape).setCenterX(event.getSceneX());
                        ((Ellipse) ashape).setCenterY(event.getSceneY());


                    }

                }
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {//拖动时的坐标

            @Override
            public void handle(MouseEvent event) {
            if (event.getEventType()==MouseEvent.MOUSE_DRAGGED){
                if (ashape instanceof Circle){
                    ((Circle) ashape).setRadius(Math.abs(event.getSceneY()-((Circle) ashape).getCenterY()));
//                    scene.setOnMouseDragged(null);
                }
                else if(ashape instanceof Rectangle){
                        ((Rectangle) ashape).setWidth(Math.abs(event.getSceneX()-((Rectangle) ashape).getX()));
                        ((Rectangle) ashape).setHeight(Math.abs(event.getSceneY()-((Rectangle) ashape).getY()));
                }else if (ashape instanceof Line){

                    ((Line) ashape).setEndX(event.getSceneX());
                    ((Line) ashape).setEndY(event.getSceneY());

                } else if (ashape instanceof Ellipse) {
                    ((Ellipse) ashape).setRadiusX(Math.abs(event.getSceneX()-((Ellipse) ashape).getCenterX()));
                    ((Ellipse) ashape).setRadiusY(Math.abs(event.getSceneY()-((Ellipse) ashape).getCenterY()));
                }
            }

            }
        });

        scene.setOnMouseReleased(new EventHandler<MouseEvent>() {//释放的坐标
            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType()==MouseEvent.MOUSE_RELEASED){
                    scene.setOnMousePressed(null);
                    scene.setOnMouseDragged(null);
                }
            }
        });
//        if (i[0]==1)
//            System.out.println("yes");
//        try {
//            Thread.sleep(1000);
//            scene.setOnMouseDragged(null);
//            scene.setOnMousePressed(null);
//            scene.setOnMouseDragged(null);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }//画图，并保存
    private void jianting(Shape shape1){//拖动图形
        shape1.setOnMouseEntered(event -> {//移入待拖动图形变成移动鼠标
            if (event.getEventType()==MouseEvent.MOUSE_ENTERED){
                shape1.setCursor(Cursor.MOVE);
            }
        });
        shape1.setOnMouseExited(event -> {
            if (event.getEventType()==MouseEvent.MOUSE_EXITED)
                shape1.setCursor(Cursor.DEFAULT);
        });
        shape1.setOnMousePressed(event -> {
            if(event.getEventType()==MouseEvent.MOUSE_PRESSED&&shape1 instanceof  Line ){
                pressedx=event.getSceneX();
                pressedy=event.getSceneY();
                dertax=((Line) shape1).getStartX()-pressedx;
                dertay=((Line) shape1).getStartY()-pressedy;
                dertax2=((Line) shape1).getEndX()-pressedx;
                dertay2=((Line) shape1).getEndY()-pressedy;

            }
        });
        shape1.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {


                if(event.getEventType()==MouseEvent.MOUSE_DRAGGED){
                    if(shape1 instanceof Circle){
                        ((Circle) shape1).setCenterX(event.getSceneX());
                        ((Circle) shape1).setCenterY(event.getSceneY());
                    } else if (shape1 instanceof Rectangle) {
                        ((Rectangle) shape1).setX(event.getSceneX()-((Rectangle) shape1).getWidth()/2);
                        ((Rectangle) shape1).setY(event.getSceneY()-((Rectangle) shape1).getHeight()/2);
                    } else if (shape1 instanceof Ellipse) {
                        ((Ellipse) shape1).setCenterX(event.getSceneX());
                        ((Ellipse) shape1).setCenterY(event.getSceneY());
                    } else if (shape1 instanceof  Line ) {
                        ((Line) shape1).setStartX(event.getSceneX()+dertax);
                        ((Line) shape1).setStartY(event.getSceneY()+dertay);
                        ((Line) shape1).setEndX(event.getSceneX()+dertax2);
                        ((Line) shape1).setEndY(event.getSceneY()+dertay2);
                    }
                }

            }
        });
        shape1.setOnMouseReleased(event -> {
//            if (event.getEventType()==MouseEvent.MOUSE_RELEASED){
//                shape1.setOnMouseDragged(null);
//                shape1.setOnMousePressed(null);
//            }
        });
    }//拖动图形
    private void jianting2(Group g1,Shape ashape,HBox xuanbox){//双击选中图形组件

        ashape.setOnMouseClicked(event -> {
            if(event.getButton()== MouseButton.PRIMARY&&event.getClickCount()==2){//双击某个组件
                jianting(ashape);//双击后可以拖动
                CreateSonMenu(g1,xuanbox,ashape);//双击创建子菜单，可以改变边框和填充
                /*按钮的动作在哪定义的问题,已经在一小时后解决*/
            }
        });
    }//双击选中图形组件
    private void CreateSonMenu(Group g1,HBox xuanbox,Shape ashape){ //创建子菜单，分别定义8个颜色按钮和4个功能按钮以及动作和何时添加规则
        Button areabutton=new Button("面积周长");areabutton.setStyle("-fx-font-size:15");
        Button delbutton=new Button("取消显示");delbutton.setStyle("-fx-font-size:15");
        Label borderfill=new Label("边框：");borderfill.setStyle("-fx-font-size:15");
        Button tou=new Button("  ");tou.setMinWidth(10);tou.setMinHeight(10);tou.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-border-color:black;");
        Button black=new Button("  ");black.setMinWidth(10);black.setMinHeight(10);black.setStyle("-fx-background-color:black;");
        Button yellow=new Button("  ");yellow.setMinWidth(10);yellow.setMinHeight(10);yellow.setStyle("-fx-background-color:yellow;");
        Button green=new Button("  ");green.setMinWidth(10);green.setMinHeight(10);green.setStyle("-fx-background-color:green;");
        Button red=new Button("  ");red.setMinWidth(10);red.setMinHeight(10);red.setStyle("-fx-background-color:red;");
        Label fill=new Label("填充");fill.setStyle("-fx-font-size:15");
        Button tou2=new Button("  ");tou2.setMinWidth(10);tou2.setMinHeight(10);tou2.setStyle("-fx-background-color:rgba(0,0,0,0);-fx-border-color:black;");
        Button black2=new Button("  ");black2.setMinWidth(10);black2.setMinHeight(10);black2.setStyle("-fx-background-color:black;");
        Button yellow2=new Button("  ");yellow2.setMinWidth(10);yellow2.setMinHeight(10);yellow2.setStyle("-fx-background-color:yellow;");
        Button green2=new Button("  ");green2.setMinWidth(10);green2.setMinHeight(10);green2.setStyle("-fx-background-color:green;");
        Button red2=new Button("  ");red2.setMinWidth(10);red2.setMinHeight(10);red2.setStyle("-fx-background-color:red;");
        Button del=new Button("删除");del.setStyle("-fx-font-size:15");
        Button queren=new Button("确定");queren.setStyle("-fx-font-size:15");
        HBox box2=new HBox(20);//放置确定，删除按钮;
        box2.getChildren().addAll(del,queren);
        HBox xuanbox1=new HBox(10);

        HBox xuanbox2=new HBox(10);
        xuanbox1.getChildren().addAll(areabutton,delbutton,borderfill,tou,black,red,green,yellow);
        xuanbox2.getChildren().addAll(fill,tou2,black2,red2,green2,yellow2);
        xuanbox.setAlignment(Pos.CENTER);
        xuanbox.setMinHeight(30);
        areabutton.setOnAction(e->{
            ShowSrea(g1,xuanbox,ashape,true);
        });
        delbutton.setOnAction(e->{
            ShowSrea(g1,xuanbox,ashape,false);
        });
        tou.setOnAction(e->{
            ashape.setStroke(Color.rgb(0,0,0,0));
        });
        red.setOnAction(e->{
            ashape.setStroke(Color.RED);
        });
        black.setOnAction(e->{
            ashape.setStroke(Color.BLACK);
        });
        yellow.setOnAction(e->{
            ashape.setStroke(Color.YELLOW);
        });
        green.setOnAction(e->{
            ashape.setStroke(Color.GREEN);
        });
        tou2.setOnAction(e->{
            ashape.setFill(Color.rgb(0,0,0,0));
        });
        red2.setOnAction(e->{
            ashape.setFill(Color.RED);
        });
        black2.setOnAction(e->{
            ashape.setFill(Color.BLACK);
        });
        yellow2.setOnAction(e->{
            ashape.setFill(Color.YELLOW);
        });
        green2.setOnAction(e->{
            ashape.setFill(Color.GREEN);
        });
        del.setOnAction(e->{
            deleteShape(ashape);
            removeAction(ashape);//删除监听鼠标动作
            xuanbox.getChildren().clear();//清除子菜单
            Update(g1,xuanbox);//更新图形
        });
        queren.setOnAction(e->{
            removeAction(ashape);//删除鼠标监听动作
            xuanbox.getChildren().clear();//清除子菜单
            Update(g1,xuanbox);//更新图形显示
        });
        if (xuanbox.getChildren().size()>=1){

            xuanbox.getChildren().clear();
        }
        if(ashape instanceof Line)
            xuanbox.getChildren().addAll(xuanbox1,box2);
        else
            xuanbox.getChildren().addAll(xuanbox1,xuanbox2,box2);
    }//创建子菜单，分别定义8个颜色按钮和4个功能按钮以及动作和何时添加规则
    private void ShowSrea(Group g1,HBox xuanbox,Shape ashape,Boolean buer){//计算，并判断显示/不显示面积周长

        String s=new String();
        Label arealable=new Label();
        double centerx=0,centery=0,area=0,zhou=0,a=0,b=0,a2=0,b2=0;
        arealable.setStyle("-fx-background-color:rgb(226,226,226);-fx-text-fill:black;");
        if (ashape instanceof Circle){
            centerx=((Circle) ashape).getCenterX();
            centery=((Circle) ashape).getCenterY();
            area=Math.PI*((Circle) ashape).getRadius()*((Circle) ashape).getRadius();
            zhou=2*Math.PI*((Circle) ashape).getRadius();
        }
        if (ashape instanceof Rectangle){
            centerx=((Rectangle) ashape).getX()+((Rectangle) ashape).getWidth()/2;
            centery=((Rectangle) ashape).getY()+((Rectangle) ashape).getHeight()/2;
            area=((Rectangle) ashape).getHeight()*((Rectangle) ashape).getWidth();
            zhou=2*(((Rectangle) ashape).getWidth()+((Rectangle) ashape).getHeight());
        }
        if (ashape instanceof Ellipse){
            centerx=((Ellipse) ashape).getCenterX();
            centery=((Ellipse) ashape).getCenterY();
            area=Math.PI*((Ellipse) ashape).getRadiusX()*((Ellipse) ashape).getCenterY();
            a=((Ellipse) ashape).getRadiusX();
            b=((Ellipse) ashape).getRadiusY();
            if(a>b)
                zhou=2*Math.PI*b+4*(a-b);
            else
                zhou=2*Math.PI*a+4*(b-a);
        }
        if (ashape instanceof Line){
                centerx=(((Line) ashape).getStartX()+((Line) ashape).getEndX())/2;
                centery=(((Line) ashape).getStartY()+((Line) ashape).getEndY())/2;
                area=0;
                a=((Line) ashape).getStartX();
                b=((Line) ashape).getStartY();
                a2=((Line) ashape).getEndX();
                b2=((Line) ashape).getEndY();
                zhou=Math.sqrt((a2-a)*(a2-a)+(b-b2)*(b-b2));
        }
        s="面积:"+String.format("%.2f",area)+" 周长:"+String.format("%.2f",zhou);
        arealable.setText(s);
        arealable.setLayoutX(centerx-75);
        arealable.setLayoutY(centery-10);
        if (buer){
            Update(g1,xuanbox);
            g1.getChildren().add(arealable);
        }
        else {
            g1.getChildren().remove(arealable);
            Update(g1,xuanbox);
        }
    }//是否显示周长面积
    private void deleteShape(Shape ashape){//删除图形
        if (ashape instanceof Circle){
            for (int i=0;i<data.size();i++){
                if (((Circle) ashape).equals(data.get(i))) {
                    data.remove(i);
                }

            }
        }
        if (ashape instanceof Rectangle){
            for (int i=0;i<data.size();i++){
                if (((Rectangle) ashape).equals(data.get(i))) {
                    data.remove(i);
                }

            }
        }
        if (ashape instanceof Ellipse){
            for (int i=0;i<data.size();i++){
                if (((Ellipse) ashape).equals(data.get(i))) {
                    data.remove(i);
                }

            }
        }
        if (ashape instanceof Line){
            for (int i=0;i<data.size();i++){
                if (((Line) ashape).equals(data.get(i))) {
                    data.remove(i);
                }

            }
        }
    }//删除图形
    private void removeAction(Shape ashape){//清楚所有图形的鼠标监听事件
        ashape.setOnMousePressed(null);
        ashape.setOnMouseEntered(null);
        ashape.setOnMouseExited(null);
        ashape.setOnMouseDragged(null);//双击事件结束后，
        ashape.setOnMouseReleased(null);
    }//清楚所有图形的鼠标监听事件
    private void createMonitoredLabel(final Label reporter,Scene st) {//获取鼠标位置，并显示在屏幕
        reporter.setStyle("-fx-text-fill: black; -fx-font-size: 20px;");
        st.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String msg ="";
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED){
                    msg = "X:"  + event.getSceneX() + "Y: "  + event.getSceneY();
                    reporter.setText(msg);
                }
            }

        });
        st.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String msg ="";
                if (event.getEventType() == MouseEvent.MOUSE_DRAGGED){
                    msg = "X:"  + event.getSceneX() + "Y: "  + event.getSceneY();
                }
                reporter.setText(msg);
            }
        });
        st.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String msg = "";
                if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                    msg = "X:"  + event.getSceneX() + "Y: "  + event.getSceneY();
                }
                reporter.setText(msg);
            }
        });
    st.setOnMouseMoved(new EventHandler<MouseEvent>() {

        @Override public void handle(MouseEvent event) {

            String msg = "";
            if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
                msg = "X:"  + event.getSceneX() + "Y: "  + event.getSceneY();
            }
            reporter.setText(msg);
        }
    });
    }//获取鼠标位置，并显示在屏幕
    public static void main(String[] args) {
        System.out.println("success!");
        launch();
    }


}
/*初期测试功能用的，虽然不用了，他们见证了每一个功能的实现，不舍得删除了
        Circle c1 = new Circle();
        c1.setAccessibleText("nimade");
        c1.setStroke(Color.GREEN);
        c1.setFill(Color.WHITE);
        c1.setStrokeWidth(2);
        c1.setCenterY(500);
        c1.setCenterX(500);
        c1.setRadius(100);
        Rectangle R1 = new Rectangle();
        R1.setX(200);
        R1.setY(500);
        R1.setWidth(200);
        R1.setHeight(200);
        R1.setFill(Color.BLACK);
        c1.setOnMouseEntered(event -> {
            if (event.getEventType()==MouseEvent.MOUSE_ENTERED)
                scene.setCursor(Cursor.MOVE);
        });
        c1.setOnMouseExited(event -> {
            if (event.getEventType()==MouseEvent.MOUSE_EXITED)
                scene.setCursor(Cursor.DEFAULT);
        });
        data.add(c1);
        data.add(R1);
        String ss=""+Math.PI*c1.getRadius()*c1.getRadius();
        Label c2=new Label(ss);
        c2.setLayoutX(c1.getCenterX());
        c2.setLayoutY(c1.getCenterY());


*/