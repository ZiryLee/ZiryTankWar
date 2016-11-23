package me.ziry.tankWar;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 子弹类
 * @author Ziry
 */
public class Missile {
	
	/**
	 * 子弹X坐标
	 */
	private int x = 0;
	
	/**
	 * 子弹Y坐标
	 */
	private int y = 0;
	
	/**
	 * 子弹宽度
	 */
	public static final int WITDTH = 10;
	
	/**
	 * 子弹高度
	 */
	public static final int HEIGHT = 10;
	
	/**
	 * 子弹移动间隔
	 */
	private static final int DISTANCE = 15;

	/**
	 * 坦克炮筒方向
	 */
	private Tank.Direction direction;
	
	/**
	 * 子弹是否活着
	 */
	private boolean live = true;
		
	/**
	 * 大管家引用
	 */
	private TankWar tw = null;
	
	/**
	 * 哪两坦克的子弹
	 */
	private Tank t = null;
	
	
	
	//坦克image
	private static Toolkit tool = Toolkit.getDefaultToolkit();	
	private static Image[] tankImg = null;
	private static Map<String, Image> img = new HashMap<String, Image>();
	static {
		
			tankImg = new Image[] {
				tool.getImage( Missile.class.getClassLoader().getResource("images/Missile_L.jpg") ),
				tool.getImage( Missile.class.getClassLoader().getResource("images/Missile_LU.jpg") ),
				tool.getImage( Missile.class.getClassLoader().getResource("images/Missile_U.jpg") ),
				tool.getImage( Missile.class.getClassLoader().getResource("images/Missile_RU.jpg") ),
				tool.getImage( Missile.class.getClassLoader().getResource("images/Missile_R.jpg") ),
				tool.getImage( Missile.class.getClassLoader().getResource("images/Missile_RD.jpg") ),
				tool.getImage( Missile.class.getClassLoader().getResource("images/Missile_D.jpg") ),
				tool.getImage( Missile.class.getClassLoader().getResource("images/Missile_LD.jpg") )
		};
			
		img.put("L", tankImg[0]);
		img.put("LU", tankImg[1]);
		img.put("U", tankImg[2]);
		img.put("RU", tankImg[3]);
		img.put("R", tankImg[4]);
		img.put("RD", tankImg[5]);
		img.put("D", tankImg[6]);
		img.put("LD", tankImg[7]);
		
	}
	
	
	/**
	 * 子弹是否活着
	 */
	public boolean isLive() {
		return live;
	}

	
	/**
	 * 构造方法1
	 * 得到坐标及方向
	 */
	public Missile( int x, int y, Tank.Direction direction ) {
		
		this.direction = direction;
		this.x = x;
		this.y = y;	
		
	}
	
	/**
	 * 够着方法2
	 * 得到坐标、方向、方向，哪辆坦克，大管家
	 */
	public Missile(int x, int y, Tank.Direction direction, Tank t, TankWar tw) {
		this.direction = direction;
		this.x = x;
		this.y = y;	
		this.tw = tw;
		this.t = t;
	}
	
	/**
	 * 画出子弹
	 */
	public void drawMissile(Graphics g) {
		//如果没活着从管家集合中删除
		if( !live ) {
			if( tw != null ) {
				tw.missiles.remove(this);
			}
			return;
		}
		
		//根据炮筒方向画出炮弹
		switch( direction ) {
		case L :
			g.drawImage(img.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(img.get("LU"), x, y, null);
			break;
		case U :
			g.drawImage(img.get("U"), x, y, null);
			break;
		case RU:
			g.drawImage(img.get("RU"), x, y, null);
			break;
		case R :
			g.drawImage(img.get("R"), x, y, null);
			break;
		case RD:
			g.drawImage(img.get("RD"), x, y, null);
			break;
		case D :
			g.drawImage(img.get("D"), x, y, null);
			break;
		case LD:
			g.drawImage(img.get("LD"), x, y, null);
			break;
		default:
			break;	
		}		
		
		goDirection();
		
	}
	
	/**
	 * 子弹轨迹
	 */
	public void goDirection() {
		

		switch( direction ) {
		case L :
			x -= DISTANCE;
			break;
		case LU:
			x -= DISTANCE;
			y -= DISTANCE;
			break;
		case U :
			y -= DISTANCE;
			break;
		case RU:
			x += DISTANCE;
			y -= DISTANCE;
			break;
		case R :
			x += DISTANCE;
			break;
		case RD:
			x += DISTANCE;
			y += DISTANCE;
			break;
		case D :
			y += DISTANCE;
			break;
		case LD:
			x -= DISTANCE;
			y += DISTANCE;
			break;
		case STOP:
			break;
		default:
			break;
		}
		
		//超出屏幕死去
		if(x < 0 || y < 0 || x > TankWar.GAME_WIDTH || y > TankWar.GAME_HEIGHT) {
			live = false;			
		}
		
	}

	/**
	 * 得到所在方块
	 * 返回 Rectangle对象
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, WITDTH, HEIGHT);
	}
		
	
	/**
	 * 子弹碰墙
	 */
	public boolean hitWall(Wall w) {
		if( this.live && this.getRect().intersects( w.getRect() )) {
			live = false;
			return true;
		}
		return false;
	}
	
	
	/**
	 * 子弹碰坦克
	 * 子弹活着 且 相碰坦克 且 坦克活着 且 是非同军
	 * 子弹死去 
	 * 如果是好坦克则口20%血否者死去
	 * 增加炮炸效果
	 */
	public boolean hitTank(Tank t) {
		
		//子弹活着 且 相碰坦克 且 坦克活着 且 是非同军
		if(this.live && this.getRect().intersects( t.getRect() ) 
				&& t.isLive() && this.t.good != t.good) {
			//子弹死去
			live = false;
			
			//如果是好坦克则口20%血否者死去
			if( t.good ) {
				
				t.setPh( t.getPh()-20 );
				
				if( t.getPh() <= 0 ) {
					
					t.setLive(false);
					
				}
				
			} else {
				
				t.setLive(false);
				
			}
			
			//增加炮炸效果
			tw.explodes.add( new Explode(x, y, tw) );
			
			return true;
			
		}
		
		return false;
	}
	
	/**
	 * 子弹碰坦克集合
	 * 得到集合中每辆坦克
	 * 如果子弹碰到坏坦克则从集合中删除
	 * 并返回true否者返回false
	 */
	public boolean hitTanks(List<Tank> ts) {
		
		for (int i = 0; i < ts.size(); i++) {
			Tank t = ts.get(i);
			/*
			 * 得到集合中每辆坦克
			 * 如果子弹碰到坏坦克则从集合中删除
			 * 并返回true否者返回false
			 */
			if ( hitTank(t) ){
				if( !t.isGood() ) {
					tw.tanks.remove(t);
				}
				return true;
			}
		}
		
		return false;
	}

}
