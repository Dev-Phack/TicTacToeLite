package phack.megagame.tictactoeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView playerOneScore, playerTwoScore, playerStatus;
    private Button[] buttons = new Button[9];
    private Button resetGame;
    private Button goOn;

    private int playerOneScoreCount, playerTwoScoreCount, rountCount;
    boolean activePlayer;
    boolean blocus = false;

    //player1 => 0
    //player2 => 1
    //empty => 2
    int [] gameState = {2,2,2,2,2,2,2,2,2};

    int[][] winningPositions = {
            {0,1,2},{3,4,5},{6,7,8},//rows
            {0,3,6},{1,4,7},{2,5,8},//columns
            {0,4,8},{2,4,6}//cross
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        playerOneScore = (TextView) findViewById(R.id.playerOneScore);
        playerTwoScore = (TextView) findViewById(R.id.playerTwoScore);
        playerStatus = (TextView) findViewById(R.id.playerStatus);

        resetGame = (Button) findViewById(R.id.resetGame);
        goOn = (Button) findViewById(R.id.goOn);

        for (int i=0; i<buttons.length; i++){
            String buttonID = "btn_"+i;
            int resourceID = getResources().getIdentifier(buttonID,"id", getPackageName());
            buttons[i] = (Button) findViewById(resourceID);
            buttons[i].setOnClickListener(this);
        }
        rountCount = 0;
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        activePlayer = true;

    }

    @Override
    public void onClick(View view) {
        if (!((Button)view).getText().toString().equals("")){
            return;
        }
        String buttonID = view.getResources().getResourceEntryName(view.getId());//example: return btn_2
        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length()-1, buttonID.length()));//return 2

        if (activePlayer && !blocus){
            ((Button) view).setText("X");
            ((Button) view).setTextColor(Color.parseColor("#FFC34A"));
            gameState[gameStatePointer] = 0;
        }
        else if (!activePlayer && !blocus){
            ((Button) view).setText("O");
            ((Button) view).setTextColor(Color.parseColor("#70FFEA"));
            gameState[gameStatePointer] = 1;
        }
        rountCount++;

        if (checkWinner()){
            if (activePlayer && !blocus){
                playerOneScoreCount++;
                updatePlayerScore();
                Toast.makeText(this, "Player one won!", Toast.LENGTH_SHORT).show();
//                playAgain();
            }else if (!activePlayer && !blocus){
                playerTwoScoreCount++;
                updatePlayerScore();
                Toast.makeText(this, "Player two won!", Toast.LENGTH_SHORT).show();
//                playAgain();
            }
            blocus = true;
        }else if (rountCount == 9){
            blocus = true;
//            playAgain();
            Toast.makeText(this, "No winner!", Toast.LENGTH_SHORT).show();
        }else {
            activePlayer = !activePlayer;
        }

        goOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAgain();
                blocus = false;
            }
        });

        if (playerOneScoreCount > playerTwoScoreCount){
            playerStatus.setText("Player One is winning!");
        }else if (playerTwoScoreCount > playerOneScoreCount){
            playerStatus.setText("Player two is winning!");
        }else{
            playerStatus.setText("");
        }

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAgain();
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                playerStatus.setText("");
                updatePlayerScore();
            }
        });

    }

    public boolean checkWinner(){
        boolean winnerResult = false;

        for (int [] winningPosition : winningPositions){
            if(gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2){
                winnerResult = true;
            }
        }
        return winnerResult;
    }

    public void updatePlayerScore(){
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
    }

    public void  playAgain(){
        rountCount = 0;
        activePlayer = true;

        for (int i = 0; i < buttons.length; i++){
            buttons[i].setText("");
            gameState[i] = 2;
        }
    }

}