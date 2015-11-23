package prefab;

import Main.SimpleSlickGame;

public class MyBullet extends GameObject {
	
	public int power;

	public MyBullet(SimpleSlickGame scene, float posX, float posY, int kind, int index, int uniqueId){
		
		super(scene, posX, posY, kind, index, uniqueId);
		scene.efm[0].play();
	}
	
	@Override
	public boolean move() {
		// TODO Auto-generated method stub
		
		if(posY>-60)
			return true;
		
		return false;
	}

}
