package prefab;

import Main.SimpleSlickGame;


public class EnemyBullet extends GameObject {
	
	int cnt;
	float playerX, playerY;

	public EnemyBullet(SimpleSlickGame scene, float posX, float posY, int kind, int index, int uniqueId, float playerX, float playerY){

		super(scene, posX, posY, kind, index, uniqueId);

		this.playerX = playerX;
		this.playerY = playerY;

		switch(index){
		case 0:
			this.speed = 3.0f;
			this.degree = 180;
			break;
		case 1:
			this.speed = 10.0f;
			break;
		case 2:
			this.speed = 1.0f;
			break;
		case 3:
			this.speed = 2.0f;
			this.degree = this.getAngle(playerX, playerY); 
			break;
		}
	}
	
	@Override
	public boolean move() {
		// TODO Auto-generated method stub
		
		cnt++;
		
		//간단하게 총알에 변화를 준다
		switch(this.index){
		case 0://노멀총알
			break;
		case 1://점점 느려지는 총알
			if(this.speed>1.0f)
				this.speed -= 0.07f;
			break;
		case 2://점점 빨라지는 총알
			if(this.speed<15.0f)
				this.speed += 0.07f;
			break;
		case 3://일정시간동안은 플레이어를 추적하는 총알
			if(cnt<200){
				if(cnt%50==0){
					this.degree = getAngle(this.playerX, this.playerY); 
				}
			}
			break;
		}
		
		
		if(posX<-10 || posX > 490 || posY<-10 || posY>650)
			return false;
		
		return true;
	}
	
	//총알과 특정 포인트(주로 플레이어의 위치) 사이의 각도 구하기
	public int getAngle(float dx, float dy){

		double rad = Math.atan2((dx - this.posX), (dy - posY));
		int degree = (int)((rad*180)/Math.PI);
		
		return degree;
	}

}
