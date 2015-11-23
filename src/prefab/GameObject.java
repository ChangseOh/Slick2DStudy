package prefab;

import java.io.Serializable;

import Main.SimpleSlickGame;

abstract public class GameObject  implements Serializable{

	transient public SimpleSlickGame scene;
	public float posX, posY;
	public int uniqueId;
	public int kind;
	public int index;
	public int degree;
	public float speed;
	public boolean isDelete;
	
	public GameObject(SimpleSlickGame scene, float posX, float posY, int kind, int index, int uniqueId){
		
		this.scene = scene;
		this.posX = posX;
		this.posY = posY;
		this.kind = kind;
		this.index = index;
		this.uniqueId = uniqueId;
		this.isDelete = false;
	}
	
	abstract public boolean move();
}
