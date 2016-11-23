package me.ziry.tankWar;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 坦克
 *	有好坏坦克
 */

public class Tank {

	//位置
	/**
	 * 坦克X轴位置
	 */
	private int x = 50;
	
	/**
	 * 坦克Y轴位置
	 */
	private int y = 50;
	
	/**
	 * 上一步X位置
	 * 用来越界还原
	 */
	private int oldX ;
	
	/**
	 * 上一步Y位置
	 * 用来越界还原
	 */
	private int oldY ;
	
	/**
	 * 坦克移动间隔
	 */
	public static final int DISTANCE = 5;
	
	/**
	 * 坦克宽度
	 */
	public static final int WITDTH = 30;
	
	/**
	 * 坦克高度
	 */
	public static final int HEIGHT = 30;	
		
	/**
	 * 坦克好坏
	 */
	public boolean good ;
	
	/**
	 * 坦克是否活着
	 */
	public boolean live = true;
	
	/**
	 * 坦克Ph值（血）
	 */
	private int ph = 100;
	
	/**
	 * 大管家引用
	 */
	TankWar tw = null;
	
	/**
	 * 随机器
	 * 产生随机数
	 * 用于敌军自动移动和开火
	 */
	private static Random random = new Random();	
	
	/**
	 * 产生一个3~14的随机数用于敌军自动移动
	 */
	int countBu = random.nextInt(12)+3;
	
		/**
	 * 坦克移动方向，8个方向
	 */
	enum Direction {L, LU, U, RU, R, RD, D, LD, STOP};
	
	/**
	 * 那个键被按下
	 */
	private boolean bL=false, bU=false, bR=false, bD=false;
	
	/**
	 * 坦克初始方向
	 */
	Direction direction = Direction.STOP;
	
	/**
	 * 坦克炮筒方向
	 */
	Direction ptDir = Direction.L;
	
	/**
	 * 控制超级炮弹数量	 
	 */
	
	int superFireNum = Integer.parseInt( TankWarProper.getProper("superFireNum") );
	int superFireCount = superFireNum ;
	
	
	//坦克image
	private static Toolkit tool = Toolkit.getDefaultToolkit();	
	private static Image[] tankImg = null;
	private static Map<String, Image> img = new HashMap<String, Image>();
	static {
		
			tankImg = new Image[] {
				tool.getImage( Tank.class.getClassLoader().getResource("images/Tank_L.jpg") ),
				tool.getImage( Tank.class.getClassLoader().getResource("images/Tank_LU.jpg") ),
				tool.getImage( Tank.class.getClassLoader().getResource("images/Tank_U.jpg") ),
				tool.getImage( Tank.class.getClassLoader().getResource("images/Tank_RU.jpg") ),
				tool.getImage( Tank.class.getClassLoader().getResource("images/Tank_R.jpg") ),
				tool.getImage( Tank.class.getClassLoader().getResource("images/Tank_RD.jpg") ),
				tool.getImage( Tank.class.getClassLoader().getResource("images/Tank_D.jpg") ),
				tool.getImage( Tank.class.getClassLoader().getResource("images/Tank_LD.jpg") )
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
	 * 构造坦克
	 * 位置
	 * 好坏
	 */
	public Tank(int x, int y, boolean good) {
		
		this.x = x;
		this.y = y;
		this.good = good;
		this.oldX = x;
		this.oldY = y;
		
	}
	
	/**
	 * 构造坦克
	 * 位置
	 * 好坏
	 * 大管家
	 */
	public Tank(int x, int y, boolean good, TankWar tw){
		this(x, y, good);
		this.tw = tw;
	}	
	
	/**
	 * 得到血量
	 */
	public int getPh() {
		return ph;
	}
	
	/**
	 * 设置血量
	 */
	public void setPh(int ph) {
		this.ph = ph;
	}
	
	/**
	 * 坦克是否活着
	 */
	public boolean isLive() {
		return live;
	}

	/**
	 * 设置坦克生死
	 */
	public void setLive(boolean live) {
		this.live = live;
	}
	
	/**
	 * 是否为好坦克
	 */
	public boolean isGood() {
		return good;
	}	
	
	/**
	 * 增加坏坦克
	 * @param i 坦克数量
	 */

	public void addTank(int i) {
		
		for (int j = 0; j < i; j++) {
			
			Tank t = new Tank(80 + 50*j, 50, false, tw);
			//如果此位置没坦克这增加，防止重叠
			if( !t.hitTanks(tw.tanks) ){
				tw.tanks.add(t);
			}
		}
		
	}
	
	/**
	 * 绘制坦克
	 */
	public void drawTank(Graphics g) {
		
		//如果已死去不画
		if( !live ) {
			return;
		}
		
		//如果是好坦克话血量
		if( good ) {
			this.drawHp(g);
		}
		
		//用于还原
		Color c = g.getColor();
		
		//控制好坏坦克颜色
		if( good ) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLUE);
		}
				
		//按照制定方向画出炮筒
		switch( ptDir ) {
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
		
		//还原颜色
		g.setColor(c);
		
		//下一步
		goDirection();
	}
	
	/**
	 * 画出Ph条
	 */	
	public void drawHp(Graphics g) {
		
		Color c = g.getColor();
		g.setColor(Color.RED);
		g.drawRect(x-2, y-4, WITDTH+4, 4);
		int w2 = (WITDTH+4)*ph / 100;
		g.fillRect(x-2, y-4, w2, 4);
		g.setColor(c);
		
	}
	
	/**
	 * 向哪个方向执行
	 */
	public void goDirection() {
		
		//保留上一步方向
		oldX = x;
		oldY = y;
		
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
		default:
			break;	
		}
		
		//如果不是STOP则传给炮筒方向
		if( direction != Direction.STOP) {
			ptDir = direction;
		}
		
		//防止超出屏幕
		if( x < 0 ) {
			x = 0;
		}
		if( y < 30 ) {
			y = 30;
		}
		if( x + Tank.WITDTH > TankWar.GAME_WIDTH ) {
			x = TankWar.GAME_WIDTH - Tank.WITDTH;
		}
		if( y + Tank.HEIGHT > TankWar.GAME_HEIGHT ) {
			y = TankWar.GAME_HEIGHT - Tank.HEIGHT;
		}

		//如果是坏坦克随机移动随机开火
		if( !good ) {
			//随机移动
			if(countBu == 0) {
				countBu = random.nextInt( Integer.parseInt(TankWarProper.getProper("countBu"))+3);
				Direction[] dir = Direction.values();
				//1~8随意一个方向
				int n = random.nextInt(dir.length);
				direction = dir[n];
				
			}
			countBu--;
			//随机开火
			if (random.nextInt(100) > Integer.parseInt(TankWarProper.getProper("difficulty"))) {
				fire();
			}
		}
		
	}
	
	/**
	 * 判断哪些键被按下
	 */	
	public void isDirection() {
		
		if( bL && !bU && !bR && !bD ) {
			direction = Direction.L;
		} else if( bL && bU && !bR && !bD ) {
			direction = Direction.LU;
		} else if( !bL && bU && !bR && !bD ) {
			direction = Direction.U;
		} else if( !bL && bU && bR && !bD ) {
			direction = Direction.RU;
		} else if( !bL && !bU && bR && !bD ) {
			direction = Direction.R;
		} else if( !bL && !bU && bR && bD ) {
			direction = Direction.RD;
		} else if( !bL && !bU && !bR && bD ) {
			direction = Direction.D;
		} else if( bL && !bU && !bR && bD ) {
			direction = Direction.LD;
		} else if ( !bL && !bU && !bR && !bD) {
			direction = Direction.STOP;
		}
		goDirection();
	}
	
	/**
	 * 当键被按下
	 */
	public void keyPressed(KeyEvent e) {
		
		if( e.getKeyCode() == KeyEvent.VK_UP  ) {
			bU = true;
		}
		if( e.getKeyCode() == KeyEvent.VK_DOWN  ) {
			bD = true;
		}
		if( e.getKeyCode() == KeyEvent.VK_LEFT  ) {
			bL = true;
		}
		if( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
			bR = true;
		}

		isDirection();		
	}
	
	
	/**
	 * 当键被弹起
	 */
	public void keyReleased(KeyEvent e) {
		
		if( e.getKeyCode() == KeyEvent.VK_F2 ) {
			if( !live ) {
				this.setLive(true);
				this.setPh(100);
			}
		}
		if( e.getKeyCode() == KeyEvent.VK_F1 ) {
			superFire();
		}
		if( e.getKeyCode() == KeyEvent.VK_V ) {
			fire();
		}
		if( e.getKeyCode() == KeyEvent.VK_F3 ) {
			addTank(5);
		}
		if( e.getKeyCode() == KeyEvent.VK_UP  ) {
			bU = false;
		}
		if( e.getKeyCode() == KeyEvent.VK_DOWN  ) {
			bD = false;
		}
		if( e.getKeyCode() == KeyEvent.VK_LEFT  ) {
			bL = false;
		}
		if( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
			bR = false;
		}

		isDirection();	
	}
	
	
	/**
	 * 开火
	 * @return	当前子弹
	 */
	public Missile fire() {
		//死去则不开火
		if( !live )  {
			return null;
		}
		Missile missile = new Missile(x+10, y+10, ptDir, this, tw);
		tw.missiles.add(missile);
		return missile;
	}
	
	/**
	 * 开火
	 * @param dir	方向
	 * @return	当前子弹
	 */
	public Missile fire( Direction dir ) {
		//死去则不开火
		if( !live )  {
			return null;
		}
		Missile missile = new Missile(x+10, y+10, dir, this, tw);
		tw.missiles.add(missile);
		return missile;
	}

	
	/**
	 * 超级炮弹
	 */
	public void superFire() {	
						
		superFireCount--;
		
		if(superFireCount > 0 && superFireCount <= superFireNum) {
			//八个方向各一发子弹
			Direction[] dir = Direction.values();
			for (int i = 0; i < dir.length-1; i++) {
				fire(dir[i]);
			}
			
		} else {
			//每格6秒15发超级炮弹
			new Thread(new Runnable() {
				public void run() {				
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					superFireCount = superFireNum;
				}			
			}).start();
			
		}
		
	}
	
	/**
	 * 停住
	 * 当前XY等于上一步XY
	 */
	public void  stay() {
		x = oldX;
		y = oldY;
	}
	
	/**
	 * 得到所在方块
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y, WITDTH, HEIGHT);
	}
	
	/**
	 * 是否碰到墙
	 * 如果坦克活着&&是坏坦克&&碰到墙则停住
	 * @param w		墙
	 * @return		碰到true否则false
	 */
	public boolean hitWall(Wall w) {
		if( this.live && !good && this.getRect().intersects( w.getRect() ) ) {
			stay();
			return true;
		}
		return false;
	}
	
	/**
	 * 是否碰到坦克
	 * @param t		坦克
	 * @return		碰到true否则false
	 */
	public boolean hitTank(Tank t) {
		if( this.live && this.getRect().intersects( t.getRect() )&& this != t ) {
			this.stay();
			t.stay();
			return true;
		}
		return false;
	}
	
	/**
	 * 是否碰到坦克集合
	 * @param ts	哪个集合
	 * @return		碰到true否则false
	 */
	public boolean hitTanks(java.util.List<Tank> ts) {
		Tank t;
		for (int i = 0; i < ts.size(); i++) {
			t = ts.get(i);
			if(this.hitTank(t)) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 是否碰到血块
	 * 如果血块活着&&是好坦克&&碰到血块则加血50%
	 * @param ap	哪个血块
	 * @return		碰到返回true否则false
	 */
	public boolean hitPh(AddPh ap) {
		
		if( ap.isLive() && good && this.getRect().intersects( ap.getRect() ) ) {
			
			int add  = this.getPh() + 50 ;
			if(add > 100 ) {
				add = 100;
				
			} 
			this.setPh(add);
			ap.setLive(false);
			return true;
		}
		
		return false;
		
	}
	

}
