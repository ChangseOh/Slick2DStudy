package prefab;

import java.awt.Rectangle;

import Main.SimpleSlickGame;

public class UFOBoss extends GameObject {

	public int hp;
	public int cnt;
	int _degree;
	MyShip myship;
	
	public UFOBoss(SimpleSlickGame scene, float posX, float posY, int kind, int index, int uniqueId){
		
		super(scene, posX, posY, kind, index, uniqueId);
		this.hp = 10;
		_degree = 0;
		myship = (MyShip)scene.getUniqueObject(scene.objList, 0);
	}
	
	@Override
	public boolean move() {
		// TODO Auto-generated method stub
		
		if(!scene.isHost)//클라이언트가 호스트 모드가 아니라면 추가 처리는 하지 않는다
			return true;
		
		cnt++;
		
		if(this.posY < 0)
			this.posY+=0.4f;//화면 위에 등장
		else{
			//등장이 끝났으므로 공격
			
			if(50<=cnt%1000 && cnt%1000<=250){
				
				if(cnt%20==0){
					shootType1(_degree);
					_degree += 3;
				}
			}else
			if(300<=cnt%1000 && cnt%1000<=500){
				_degree = 270;
				if(cnt%10==0){
					shootType2();
				}
				
			}else
			if(550<=cnt%1000 && cnt%1000<=950){
				if(cnt%5==0){
					shootType3(_degree);
					_degree -= 15;
				}
			}else
				_degree = 0;
		}
		
		//플레이어의 총알에 맞았는가 확인과 처리
		//현시점에서는 착탄 체크만 하고 보스의 파괴 처리는 하지 않는다
		for(int i=scene.objList.size()-1; i>=0; i--){
			GameObject gob = (GameObject)scene.objList.elementAt(i);
			if(gob.kind != 3)
				continue;
			
			Rectangle rect1 = new Rectangle((int)gob.posX, (int)gob.posY, 13,33);
			Rectangle rect2 = new Rectangle(0, 0, 480,100);
			
			if(rect1.intersects(rect2)){
				//착탄 이펙트 발동
				scene.createEffect(gob.posX+6, gob.posY+6, 1);
				
				//총알의 공격력으로 hp 가감
//				this.hp -= ((MyBullet)gob).power;
				
				//총알 소멸
				gob.isDelete = true;
				//scene.objList.remove(gob);
				
				//hp가 0 이하면 파괴이므로 폭염 생성하고 false 리턴
//				if(this.hp<=0){
//					for(int j=0;j<4;j++)
//						scene.createEffect(this.posX+30 + scene.rand.nextInt(50)-25, this.posY+26 + scene.rand.nextInt(50)-25, 2);
//					return false;
//				}
			}
		}
		
		return true;
	}

	void shootType1(int _degree){//화면 아래로 반원형 일제사격
		
		for(int i=90;i<270;i+=10){
			
			EnemyBullet eb = new EnemyBullet(scene, 240-7, 100,
					4, 0, scene.uniqueId++, myship.posX, myship.posY);

			eb.degree = i + _degree;
			eb.speed = 3.0f;
			scene.objList.add(eb);
		}
	}
	
	void shootType2(){//수직으로 속도가 다른 총알 랜덤 발사

		EnemyBullet eb = new EnemyBullet(scene, scene.rand.nextInt(440)+20, 100,
				4, 0, scene.uniqueId++, myship.posX, myship.posY);

		eb.degree = 180;
		eb.speed = (float)scene.rand.nextInt(20)/5.0f + 3.0f;
		scene.objList.add(eb);
	}

	void shootType3(int _degree){//나선샷
		
		EnemyBullet eb = new EnemyBullet(scene, 240-7, 100,
				4, 0, scene.uniqueId++, myship.posX, myship.posY);

		eb.degree = _degree;
		eb.speed = 2.5f;
		scene.objList.add(eb);
	}
}
