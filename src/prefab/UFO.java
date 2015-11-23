package prefab;

import java.awt.Rectangle;

import Main.SimpleSlickGame;

public class UFO extends GameObject{

	public int hp;
	public int cnt;
	
	public UFO(SimpleSlickGame scene, float posX, float posY, int kind, int index, int uniqueId){
		
		super(scene, posX, posY, kind, index, uniqueId);
		this.hp = 10;
	}
	
	public boolean move(){
		
		if(!scene.isHost)//클라이언트가 호스트 상태가 아니라면 움직임 이외의 별도의 처리는 하지 않는다
			return true;
		
		cnt++;
		
		if(cnt==150){
			if(posX<240)
				degree = 315;
			else
				degree = 45;
		}
		
		if(cnt==250)
			degree = 180;
			
		
		if(posY > 640)
			return false;

		//총알 발사 처리
		if(this.cnt % 40 == 39){
			
			MyShip myship = (MyShip)scene.getUniqueObject(scene.objList, 0);
			EnemyBullet eb = new EnemyBullet(scene, 
					this.posX+23, this.posY+50,
					4, scene.rand.nextInt(4), scene.uniqueId++,
					myship.posX, myship.posY);
			
			if(eb.index==1 || eb.index==2)
				eb.degree = scene.cnt%360;
			
			scene.objList.add(eb);
		}
		
		//플레이어의 총알에 맞았는가 확인과 처리
		for(int i=scene.objList.size()-1; i>=0; i--){
			GameObject gob = (GameObject)scene.objList.elementAt(i);
			if(gob.kind != 3)
				continue;
			
			Rectangle rect1 = new Rectangle((int)gob.posX, (int)gob.posY, 13,33);
			Rectangle rect2 = new Rectangle((int)this.posX, (int)this.posY, 60,52);
			
			if(rect1.intersects(rect2)){
				//착탄 이펙트 발동
				scene.createEffect(gob.posX+6, gob.posY+6, 1);
				
				//총알의 공격력으로 hp 가감
				this.hp -= ((MyBullet)gob).power;
				
				//총알 소멸
				gob.isDelete = true;
				//scene.objList.remove(gob);
				
				//UFO hp가 0 이하면 UFO 파괴이므로 폭염 생성하고 false 리턴
				if(this.hp<=0){
					for(int j=0;j<4;j++)
						scene.createEffect(this.posX+30 + scene.rand.nextInt(50)-25, this.posY+26 + scene.rand.nextInt(50)-25, 2);
					scene.efm[1].play();
					return false;
				}
			}
		}
		
		return true;
	}
}
