package prefab;

import java.awt.Rectangle;

import Main.SimpleSlickGame;

public class MyShip extends GameObject {
	
	public final static int UP_PRESSED = 0x001;
	public final static int DOWN_PRESSED = 0x002;
	public final static int LEFT_PRESSED = 0x004;
	public final static int RIGHT_PRESSED = 0x008;

	int cnt;
	public int keybuff;

	public MyShip(SimpleSlickGame scene, float posX, float posY, int kind, int index, int uniqueId){
		
		super(scene, posX, posY, kind, index, uniqueId);
		this.keybuff = 0;
		this.speed = 2.8f;
		this.degree = 0;
		
		cnt = 0;
	}
	
	@Override
	public boolean move() {
		// TODO Auto-generated method stub
		
		cnt++;
		
		if(keybuff==0)
			this.speed = 0;
		else
			this.speed = 2.8f;
		
		switch (keybuff) {
		case LEFT_PRESSED:
			this.degree = 90;
			break;
		case RIGHT_PRESSED:
			this.degree = 275;
			break;
		case UP_PRESSED:
			this.degree = 0;
			break;
		case DOWN_PRESSED:
			this.degree = 180;
			break;
		case UP_PRESSED|LEFT_PRESSED:
			this.degree = 45;
			break;
		case UP_PRESSED|RIGHT_PRESSED:
			this.degree = 315;
			break;
		case DOWN_PRESSED|LEFT_PRESSED:
			this.degree = 135;
			break;
		case DOWN_PRESSED|RIGHT_PRESSED:
			this.degree = 225;
			break;
		}		

		if(!scene.isHost)//클라이언트가 호스트 모드가 아니라면 아래 처리는 하지 않는다
			return true;
		
		//플레이어의 총알을 발사한다
		if(this.cnt%5==1){
			MyBullet _bullet = new MyBullet(scene, posX+29, posY-10, 3, 0, scene.uniqueId++);
			_bullet.degree = 0;
			_bullet.speed = 12.0f;
			_bullet.power = 1;
			scene.objList.add(_bullet);
			
			scene.createEffect(posX+35, posY, 0);
		}

		//적 총알에 맞았는가 확인과 처리
		for(int i=scene.objList.size()-1; i>=0; i--){
			GameObject gob = (GameObject)scene.objList.elementAt(i);
			if(gob.kind != 4)
				continue;
			
			Rectangle rect1 = new Rectangle((int)gob.posX+2, (int)gob.posY+2, 11,11);
			Rectangle rect2 = new Rectangle((int)this.posX+19, (int)this.posY+20, 31,31);
			
			if(rect1.intersects(rect2)){
				//착탄 이펙트 발동
				scene.createEffect(gob.posX+6, gob.posY+6, 1);
				
				//플레이어 데미지 처리(현재 생략)
				
				//총알 소멸
				scene.objList.remove(gob);
				
				//플레이어 데미지에 따라 플레이어 소멸 처리 (현재 생략)
			}
		}
		
		return true;
	}

}
