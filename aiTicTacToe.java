import java.util.*;
public class aiTicTacToe {

	public int player; //1 for player 1 and 2 for player 2
	public int opponent;
	private List<List<positionTicTacToe>>  winningLines; 
	private int winvalue=100000;
	private int losevalue=-100000;
	private int totalmoves=0;
	private final int AI_DEPTH = 3;

	private int getStateOfPositionFromBoard(positionTicTacToe position, List<positionTicTacToe> board)
	{
		//a helper function to get state of a certain position in the Tic-Tac-Toe board by given position TicTacToe
		int index = position.x*16+position.y*4+position.z;
		return board.get(index).state;
	}
	public positionTicTacToe myRandomAlgorithm(List<positionTicTacToe> board, int player) 
    {
      positionTicTacToe myNextMove = new positionTicTacToe(0,0,0);
      do
      {
          Random rand = new Random();
          int x = rand.nextInt(4);
          int y = rand.nextInt(4);
          int z = rand.nextInt(4);
          myNextMove = new positionTicTacToe(x,y,z);
      }while(getStateOfPositionFromBoard(myNextMove,board)!=0);
      
      return myNextMove;
    }
	/*
	 * simple and obvious rules to check winning move and blocking move, so we can avoid running Minimax
	 */
	public positionTicTacToe rulebased(List<positionTicTacToe> board) {
	  int s0,s1,s2,s3;
	  boolean opp3=false;
	  positionTicTacToe block=new positionTicTacToe(0,0,0);
      List<positionTicTacToe> winpoints;
      positionTicTacToe win0, win1, win2, win3;
      for (int i=0;i<winningLines.size();i++) {
        winpoints = winningLines.get(i);
        win0 = winpoints.get(0);
        win1 = winpoints.get(1);
        win2 = winpoints.get(2);
        win3 = winpoints.get(3);
        s0 = getStateOfPositionFromBoard(win0,board);
        s1 = getStateOfPositionFromBoard(win1,board);
        s2 = getStateOfPositionFromBoard(win2,board);
        s3 = getStateOfPositionFromBoard(win3,board);
        positionTicTacToe[] winP= {win0,win1,win2,win3};
        int[] states= {s0,s1,s2,s3};
        int ai=0, opp=0;
        for (int s:states) {
          if (s!=0) {
              if (s==this.player) {
                ai++;
                
              }
              else {            
                opp++;           
              }             
          }
          
        }
        if ((ai==0 && opp==0)||(ai>0 && opp>0)) {
          continue;
        }
        // check if there is winning move
        if(ai==3) {
          for(int j=0;j<4;j++) {
            if(states[j]==0) {
              positionTicTacToe wp=winP[j];
              return new positionTicTacToe(wp.x,wp.y,wp.z);         
            }            
          }       
        }
        // check if we need to block
        else if (opp==3) {            
          opp3=true;
          for(int j=0;j<4;j++) {
            if(states[j]==0) {
              positionTicTacToe wp=winP[j];
              block = new positionTicTacToe(wp.x,wp.y,wp.z);         
            }
          }
        }       
        
      }
      
      if(opp3) {
        
        return block;
      }
	  
	  return new positionTicTacToe(0,0,0,-2);
	}
	
	/*
	 * Heuristic Function: For all 76 winning lines, check the marks 
	 * in each line that contains only one type of mark and assign a 
	 * value based on the number of marks in that line. Do an overall 
	 * sum for all 76 lines. 
	 */
	private int heuristicEval(List<positionTicTacToe> board)
	{
	 
	  int s0,s1,s2,s3,score=0;
	  List<positionTicTacToe> winpoints;
	  positionTicTacToe win0, win1, win2, win3;
	  for (int i=0;i<winningLines.size();i++) {
	    winpoints = winningLines.get(i);
	    win0 = winpoints.get(0);
	    win1 = winpoints.get(1);
	    win2 = winpoints.get(2);
	    win3 = winpoints.get(3);
	    s0 = getStateOfPositionFromBoard(win0,board);
	    s1 = getStateOfPositionFromBoard(win1,board);
	    s2 = getStateOfPositionFromBoard(win2,board);
	    s3 = getStateOfPositionFromBoard(win3,board);
	    
	    int[] states= {s0,s1,s2,s3};
	    int ai=0, opp=0;
	    
	    // count number of marks in the line
	    for (int s:states) {
	      if (s!=0) {
	          if (s==this.player) {
	            ai++;          
	          }
	          else {            
	            opp++;           
	          }	            
	      }
	    }
	    if ((ai==0 && opp==0)||(ai>0 && opp>0)) {
	      continue;
	    }   
	    // for AI
	    else if(ai>0) {
	      	      
	        score+=(Math.pow(4, ai));	
	      
	    }
	    // for Opponent
	    else if (opp>0) {
	      
	        score-=(Math.pow(4, opp)); 
	      
        }
	  }
	  return score;
	}
	public int minimax(List<positionTicTacToe> board, int depth, int player, int alpha, int beta) 
	{
	  int val, isEndVal;
	  
	    
	    isEndVal=isEnded(board);	
	    
	    // Check terminal state
	    if (isEndVal!=0) {    
	      if(isEndVal==-1) {
	        return 0;
	      }
	      else if(isEndVal==this.player) {
	        
	        return winvalue+depth;
	      
	      }
	      else{
	      
	        return losevalue-depth;
	      
	      } 
	    }
	  
	  // Check depth is 0
	  if (depth==0) {
	    
	    return heuristicEval(board);
	    
	  }

	  //Max
	  if (player==this.player) {
	    
	    val=Integer.MIN_VALUE;
	    
	   // generate all children
	   for (int b=0;b<board.size();b++) {
	     
	     if (board.get(b).state==0) {
	       List<positionTicTacToe> tempboard = deepCopyATicTacToeBoard(board);
	       tempboard.get(b).state=player;
	       val=Math.max(val, minimax(tempboard,depth-1,this.opponent,alpha,beta));   
	       alpha=Math.max(alpha,val);
           if (alpha>=beta) {
             break;
           }       
	     }
	   }   
	    return val;
	  }
	  
	  //Min
	  else {
	    
	    val=Integer.MAX_VALUE;
	    
	    // generate all children
	    for (int b=0;b<board.size();b++) {
	         
	         if (board.get(b).state==0) {
	           List<positionTicTacToe> tempboard = deepCopyATicTacToeBoard(board);
	           tempboard.get(b).state=player;
	           val=Math.min(val, minimax(tempboard,depth-1,this.player,alpha,beta));   
	           beta=Math.min(beta,val);
	           if (alpha>=beta) {
	             break;
	           }       
	         }
	      }
	 
	    return val;      
	  }
	  
	}
	public positionTicTacToe myAIAlgorithm(List<positionTicTacToe> board, int player)
	{

	  this.totalmoves++;
		positionTicTacToe myNextMove = new positionTicTacToe(0,0,0),check;
		int score, bestscore=Integer.MIN_VALUE;	
		
		// first move will be a random optimal first move
		if ((this.totalmoves==1)&&(isEmptyBoard(board))) {

		    return randomFirstMove(board);		  
		}
		
		/*
		 *  if you can win in 1 move, do it.
		 *  else, see if you can block any opp's next move win. 
		 */
		
		check = rulebased(board);
		if (check.state!=-2) {	
		  return check;
		}
		
		
		// Start the minimax algorithm
		for (int b=0;b<board.size();b++) {
		  
		  if (board.get(b).state==0) {
		    
		    List<positionTicTacToe> tempboard = deepCopyATicTacToeBoard(board);
		    tempboard.get(b).state=player;
		    score = minimax(tempboard,AI_DEPTH,this.opponent,Integer.MIN_VALUE,Integer.MAX_VALUE);  
		  
		    if (score>bestscore) {
              bestscore = score;
              myNextMove = board.get(b);
            }   
		  } 
		}

		return myNextMove;
	
	}
	
	//Check to see if the board is at its initial state (unmarked by any player)
	private boolean isEmptyBoard(List<positionTicTacToe> board) 
	{
	  for(int b=0;b<board.size();b++) {
	    if(board.get(b).state!=0) {
	      return false;
	    }  
	  }
	  return true;
	}
	//get a random first optimal move
	private positionTicTacToe randomFirstMove(List<positionTicTacToe> board)
	{
	  // The best first moves indices
	  int[] moves = {0,12,48,60,21,25,37,41,22,26,38,42,3,51,15,63};
	  Random rand = new Random();
	  
	  // Pick and return a random best first move
	  positionTicTacToe point = board.get(rand.nextInt(moves.length));	  
	  return point;
	  
	}
	private int[] initializeBoardScore() 
	{
	  int[] ret=new int[64];
	  int win0i,win1i,win2i,win3i;
      List<positionTicTacToe> winpoints;
      positionTicTacToe win0, win1, win2, win3;
      for (int i=0;i<winningLines.size();i++) {
        winpoints = winningLines.get(i);
        win0 = winpoints.get(0);
        win1 = winpoints.get(1);
        win2 = winpoints.get(2);
        win3 = winpoints.get(3);
        
        win0i = win0.x*16+win0.y*4+win0.z;
        win1i = win1.x*16+win1.y*4+win1.z;
        win2i = win2.x*16+win2.y*4+win2.z;
        win3i = win3.x*16+win3.y*4+win3.z;
        
        ret[win0i]++;
        ret[win1i]++;
        ret[win2i]++;
        ret[win3i]++;
      }
      return ret;
	}
	private List<positionTicTacToe> deepCopyATicTacToeBoard(List<positionTicTacToe> board)
    {
        //deep copy of game boards
        List<positionTicTacToe> copiedBoard = new ArrayList<positionTicTacToe>();
        for(int i=0;i<board.size();i++)
        {
            copiedBoard.add(new positionTicTacToe(board.get(i).x,board.get(i).y,board.get(i).z,board.get(i).state));
        }
        return copiedBoard;
    }
	private List<List<positionTicTacToe>> initializeWinningLines()
	{
		//create a list of winning line so that the game will "brute-force" check if a player satisfied any 	winning condition(s).
		List<List<positionTicTacToe>> winningLines = new ArrayList<List<positionTicTacToe>>();
		
		//48 straight winning lines
		//z axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,j,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,j,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//y axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,j,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,j,-1));
				winningLines.add(oneWinCondtion);
			}
		//x axis winning lines
		for(int i = 0; i<4; i++)
			for(int j = 0; j<4;j++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,j,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,j,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//12 main diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,0,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,1,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,2,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//yz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,0,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,3,-1));
				winningLines.add(oneWinCondtion);
			}
		//xy plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,0,i,-1));
				oneWinCondtion.add(new positionTicTacToe(1,1,i,-1));
				oneWinCondtion.add(new positionTicTacToe(2,2,i,-1));
				oneWinCondtion.add(new positionTicTacToe(3,3,i,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//12 anti diagonal winning lines
		//xz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,i,3,-1));
				oneWinCondtion.add(new positionTicTacToe(1,i,2,-1));
				oneWinCondtion.add(new positionTicTacToe(2,i,1,-1));
				oneWinCondtion.add(new positionTicTacToe(3,i,0,-1));
				winningLines.add(oneWinCondtion);
			}
		//yz plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(i,0,3,-1));
				oneWinCondtion.add(new positionTicTacToe(i,1,2,-1));
				oneWinCondtion.add(new positionTicTacToe(i,2,1,-1));
				oneWinCondtion.add(new positionTicTacToe(i,3,0,-1));
				winningLines.add(oneWinCondtion);
			}
		//xy plane-4
		for(int i = 0; i<4; i++)
			{
				List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
				oneWinCondtion.add(new positionTicTacToe(0,3,i,-1));
				oneWinCondtion.add(new positionTicTacToe(1,2,i,-1));
				oneWinCondtion.add(new positionTicTacToe(2,1,i,-1));
				oneWinCondtion.add(new positionTicTacToe(3,0,i,-1));
				winningLines.add(oneWinCondtion);
			}
		
		//4 additional diagonal winning lines
		List<positionTicTacToe> oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,3,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,0,3,-1));
		oneWinCondtion.add(new positionTicTacToe(1,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(2,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(3,3,0,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(3,0,0,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,1,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,2,-1));
		oneWinCondtion.add(new positionTicTacToe(0,3,3,-1));
		winningLines.add(oneWinCondtion);
		
		oneWinCondtion = new ArrayList<positionTicTacToe>();
		oneWinCondtion.add(new positionTicTacToe(0,3,0,-1));
		oneWinCondtion.add(new positionTicTacToe(1,2,1,-1));
		oneWinCondtion.add(new positionTicTacToe(2,1,2,-1));
		oneWinCondtion.add(new positionTicTacToe(3,0,3,-1));
		winningLines.add(oneWinCondtion);	
		
		return winningLines;
		
	}
	private int isEnded(List<positionTicTacToe> board)
    {
        //test whether the current game is ended
        
        //brute-force
        for(int i=0;i<winningLines.size();i++)
        {
            
            positionTicTacToe p0 = winningLines.get(i).get(0);
            positionTicTacToe p1 = winningLines.get(i).get(1);
            positionTicTacToe p2 = winningLines.get(i).get(2);
            positionTicTacToe p3 = winningLines.get(i).get(3);
            
            int state0 = getStateOfPositionFromBoard(p0,board);
            int state1 = getStateOfPositionFromBoard(p1,board);
            int state2 = getStateOfPositionFromBoard(p2,board);
            int state3 = getStateOfPositionFromBoard(p3,board);
            
            //if they have the same state (marked by same player) and they are not all marked.
            if(state0 == state1 && state1 == state2 && state2 == state3 && state0!=0)
            {
                //someone wins
                p0.state = state0;
                p1.state = state1;
                p2.state = state2;
                p3.state = state3;
                
                return state0;
            }
        }
        for(int i=0;i<board.size();i++)
        {
            if(board.get(i).state==0)
            {
                //game is not ended, continue
                return 0;
            }
        }
        return -1; //call it a draw
    }
	public aiTicTacToe(int setPlayer)
	{
		player = setPlayer;
		winningLines = initializeWinningLines();
		opponent = (6-setPlayer)%3;
		
	}
}
