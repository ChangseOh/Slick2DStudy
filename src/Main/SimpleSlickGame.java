package Main;
import java.awt.Point;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import prefab.GameObject;
import prefab.MyShip;
import prefab.UFO;
import prefab.UFOBoss;


public class SimpleSlickGame extends BasicGame {

	Music bgm;
	public Sound efm[];

	public boolean isHost = true;
	
	boolean isPacketTest = false;
	
	public int SCREEN_WIDTH; 
	public int SCREEN_HEIGHT; 
	
	public final static int UP_PRESSED = 0x001;
	public final static int DOWN_PRESSED = 0x002;
	public final static int LEFT_PRESSED = 0x004;
	public final static int RIGHT_PRESSED = 0x008;

	byte[] tester;
	int tempCnt;
	public Vector<GameObject> objList;
	
	Vector<Effect> effects;
	
	int keybuff;
	
	public int cnt = 0;
	public int uniqueId = 0;
	public Random rand;
	float reX;
	
	float bgPos = -640.0f;
	float bgPosTemp;
	boolean isBoss = false;
	
	public SimpleSlickGame(String gamename){
		
		super(gamename);
	}

	public void render(GameContainer arg0, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		
		g.drawImage(bg, 0, bgPos);
		
		drawObject(g);
		drawEffect(g);
	}

	
	Image bg;
	Image myship;
	Image ufo[];
	Image ufoboss;
	Image myshoot;
	Image enemyshoot;

	Image expl;
	Image spark;
	Image shootSpark;
	
	public void init(GameContainer arg0) throws SlickException {
		// TODO Auto-generated method stub
		System.out.println("init");
		
		bgm = new Music("rsc/sound/nv_01.ogg");

		String efmname[] = {
				"rsc/sound/se_shoot.ogg",
				"rsc/sound/se_crash.ogg"
		};
		efm = new Sound[efmname.length];
		
		for(int i=0;i<efmname.length;i++)
			efm[i] = new Sound(efmname[i]);
		
		SCREEN_WIDTH = 480;
		SCREEN_HEIGHT = 640;
		
		bg = new Image("rsc/longbg.jpg");
		myship = new Image("rsc/witch.png");
		ufo = new Image[2];
		ufo[0] = new Image("rsc/ufo_0.png");
		ufo[1] = new Image("rsc/ufo_1.png");
		ufoboss = new Image("rsc/boss.png");
		myshoot = new Image("rsc/shoot_0.png");
		enemyshoot = new Image("rsc/shoot_1.png");

		expl = new Image("rsc/explode.png");
		spark = new Image("rsc/eft_spark.png");
		shootSpark = new Image("rsc/fireeft.png");
		
		//myship.setFilter(Image.FILTER_LINEAR);

		objList = new Vector<GameObject>();
		objList.clear();
		
		effects = new Vector<Effect>();
		effects.clear();
		
		
		MyShip myShip = new MyShip(this, 240.0f-35, 500.0f, 0, 0, uniqueId++);
		objList.add(myShip);
		
		rand = new Random();

		bgm.setVolume(0.2f);
		bgm.loop();
		bgm.play();
		
	}

	public void update(GameContainer arg0, int arg1) throws SlickException {
		// TODO Auto-generated method stub

		cnt++;
		
		bgPos+=0.5f;
		if(bgPos>0){
			bgPos = 0.0f;
			
			if(!isBoss && isHost){//보스 생성
				System.out.println("Boss create");
				isBoss=  true;
				UFOBoss boss = new UFOBoss(this, 0, -187, 5, 0, uniqueId++);
				objList.add(boss);
			}
		}
		
		processObject();//GameObject에 공통적으로 행하는 처리
		processEffect();//이펙트 처리

		//플레이어 객체에 키 버퍼 상태를 전송한다
		MyShip myship = (MyShip)getUniqueObject(objList, 0);
		if(myship!=null)
			myship.keybuff = keybuff;
		
		
		//UFO를 발생시키는 부분
		if(!isBoss && isHost){//보스 상태이면 자코는 나오지 않는다 혹은 클라이언트가 게스트 상태이면 자체적으로 캐릭터를 생성하지 않는다
			
			if(cnt%200==50)
				reX = 10+rand.nextFloat()*460;//UFO는 4대를 일렬로 등장시킬 생각이므로, 그 기준이 되는 좌표를 하나 지정한다 
	
			//4대의 UFO를 일정 간격으로 등장시킨다
			if(cnt%200==50||cnt%200==70||cnt%200==90||cnt%200==110){
				UFO _ufo = new UFO(this, reX, -52, 2, 0, uniqueId++);
				_ufo.degree = 180;
				_ufo.speed = 2.5f;
				objList.add(_ufo);
			}
		}
	}

	
	public static void main(String[] args){
		
		try{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new SimpleSlickGame("Simple Slick Game"));
			appgc.setDisplayMode(480,  640,  false);
			appgc.setTargetFrameRate(60);
			appgc.start();
			
		}catch(SlickException e){
			Logger.getLogger(SimpleSlickGame.class.getName()).log(Level.SEVERE, null, e);
		}
	}


	
	//이미지나 그리기까지 각 클래스에 할당하는 방법도 있지만..?
	void drawObject(Graphics g){
		
		for (int i = 0; i < objList.size(); i++){
			
			GameObject buff = (GameObject)objList.elementAt(i);
			
			switch(buff.kind){
			case 0://fighter
				g.drawImage(myship, (int)buff.posX, (int)buff.posY);
				break;
			case 1://friend
				//g.drawImage(friendFighter, (int)buff.posX, (int)buff.posY);
				break;
			case 2://ufo
				g.drawImage(ufo[(cnt/30 + buff.uniqueId)%2], (int)buff.posX, (int)buff.posY);
				break;
			case 3://our shoot
				g.drawImage(myshoot, (int)buff.posX, (int)buff.posY);
				break;
			case 4://enemy shoot
				g.drawImage(enemyshoot, (int)buff.posX, (int)buff.posY);
				break;
			case 5://enemy boss
				g.drawImage(ufoboss, (int)buff.posX, (int)buff.posY);
				break;
			}
		}
	}
	
	
	public void createEffect(float posx, float posy, int kind){
		
		int maxframe[] = {4, 9, 16};
		
		Effect efct = new Effect(new Point((int)posx+rand.nextInt(3)-1, (int)posy), kind, maxframe[kind]);
		effects.add(efct);
	}
	
	void drawEffect(Graphics g){
		
		for(int i=0;i<effects.size();i++){
			
			Effect eft = effects.elementAt(i);
			
			int ecnt = eft.cnt/2;
			
			switch(eft.kind){
			case 0://발사 스파크
				drawImageAnc(g,shootSpark, eft.pos.x, eft.pos.y, (ecnt%2)*17,0, 17,30, 4);
				break;
			case 1://착탄 스파크
				drawImageAnc(g,spark, eft.pos.x, eft.pos.y, (ecnt%3)*52,(ecnt/3)*40, 52,40, 4);
				break;
			case 2://폭발 화염
				drawImageAnc(g,expl, eft.pos.x, eft.pos.y, (ecnt%4)*64,(ecnt/4)*64, 64,64, 4);
				break;
			}
		}
	}
	void processEffect(){
		
		for (int i = effects.size()-1; i >=0 ; i--){
			
			Effect efct = (Effect)effects.elementAt(i);
			
			efct.cnt++;
			if(efct.cnt/2==efct.maxframe){
				effects.remove(efct);
			}
		}
	}
	
	void processObject(){
		
		for (int i = objList.size()-1; i >=0 ; i--){
			
			GameObject buff = (GameObject)objList.elementAt(i);
			
			buff.posX -= buff.speed*(float)Math.sin(Math.toRadians(buff.degree));
			buff.posY -= buff.speed*(float)Math.cos(Math.toRadians(buff.degree));
			
			if(!buff.move() || buff.isDelete){
				//소멸
				objList.remove(buff);
			}
		}
	}
	
	//이미지를 지정 범위로 클리핑하여 그리고, 좌표 기준을 앵커에 맞춰 수정 표시
	public void drawImageAnc(Graphics gc, Image img, int x, int y, int sx,int sy, int wd,int ht, int anc){//sx,sy부터 wd,ht만큼 클리핑해서 그린다.

		if(x<0) {
			wd+=x;
			sx-=x;
			x=0;
		}
		if(y<0) {
			ht+=y;
			sy-=y;
			y=0;
		}
		if(wd<0||ht<0) return;
		x=x-(anc%3)*(wd/2);
		y=y-(anc/3)*(ht/2);
		gc.setClip(x, y, wd, ht);
		gc.drawImage(img, x-sx, y-sy);
		gc.setClip(0,0, SCREEN_WIDTH+10, SCREEN_HEIGHT+30);
	}

	public GameObject getUniqueObject(Vector<GameObject> fromList, int uniqueId){
		
		for(int i=0;i<fromList.size(); i++){
			GameObject buff = (GameObject)fromList.elementAt(i); 
			if(buff.uniqueId == uniqueId)
				return buff;
		}
		
		return null;
	}
	
	class Effect{
		
		protected Point pos;
		int cnt;
		protected int kind;
		int maxframe;
		
		Effect(Point pos, int kind, int maxframe){
			this.pos = pos;
			this.kind = kind;
			this.maxframe = maxframe;
			cnt = 0;
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		// TODO Auto-generated method stub
		switch (key) {
		case 203://KeyEvent.VK_LEFT:
			keybuff |= LEFT_PRESSED;// 멀티키의 누르기 처리
			break;
		case 205://KeyEvent.VK_RIGHT:
			keybuff |= RIGHT_PRESSED;
			break;
		case 200://KeyEvent.VK_UP:
			keybuff |= UP_PRESSED;
			break;
		case 208://KeyEvent.VK_DOWN:
			keybuff |= DOWN_PRESSED;
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		// TODO Auto-generated method stub
		switch (key) {
		case 203://KeyEvent.VK_LEFT:
			keybuff &= ~LEFT_PRESSED;// 멀티키의 누르기 처리
			break;
		case 205://KeyEvent.VK_RIGHT:
			keybuff &= ~RIGHT_PRESSED;
			break;
		case 200://KeyEvent.VK_UP:
			keybuff &= ~UP_PRESSED;
			break;
		case 208://KeyEvent.VK_DOWN:
			keybuff &= ~DOWN_PRESSED;
			break;
		default:
			break;
		}
	}



}

