package com.example.blazejapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // zmienna tablicy
    private Button[][] buttons = new Button[3][3];
    // zmienne tury gracza
    private boolean gracz1Turn = true;
    private int roundCount;
    // zmiennie punktów
    private int gracz1Points;
    private int gracz2Points;
    // zmienne pomocnicze
    private TextView textViewGracz1;
    private TextView textViewGracz2;
    @Override
    //tworzenie przycisków reset i do gry
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewGracz1 = findViewById(R.id.text_view_p1);
        textViewGracz2 = findViewById(R.id.text_view_p2);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }
    @Override
    //wszystkie akcje podejmowane po kliknięciu
    //zaznaczenie pola, dodanie rundy, sprawdzenie na wygraną, zmiana koloru
    public void onClick(View v) {

        RelativeLayout rl = (RelativeLayout)findViewById(R.id.relative_layout);

        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        if (gracz1Turn) {
            ((Button) v).setText("X");
            rl.setBackgroundColor(Color.RED);
        } else {
            ((Button) v).setText("O");
            rl.setBackgroundColor(Color.BLUE);
        }
        roundCount++;
        if (checkForWin()) {
            if (gracz1Turn) {
                gracz1Wins();
            } else {
                gracz2Wins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            gracz1Turn = !gracz1Turn;
        }
    }

    //algorytm szukająco sprawdzający do sprawdzania czy na planszy nie nastąpiła wygrana
    //to znaczy 3 pola nie zostały zaznaczone przez jedną figurę w równej linii
    private boolean checkForWin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }
        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }
        return false;
    }
    //akcje po wygranej 1 gracza -update punktów -info na ekranie -restart planszy
    private void gracz1Wins() {
        gracz1Points++;
        Toast.makeText(this, "Gracz 1 wygrywa!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }
    //akcje po wygranej 2 gracza -update punktów -info na ekranie -restart planszy
    private void gracz2Wins() {
        gracz2Points++;
        Toast.makeText(this, "Gracz 2 wygrywa!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }
    //akcje po remisie -info na ekranie -restart planszy
    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }
    //update punktów
    private void updatePointsText() {
        textViewGracz1.setText("Player 1: " + gracz1Points);
        textViewGracz2.setText("Player 2: " + gracz2Points);
    }

    //czyszczenie planszy
    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        gracz1Turn = true;
    }

    //reset gry do 0 punktów
    private void resetGame() {
        gracz1Points = 0;
        gracz2Points = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount", roundCount);
        outState.putInt("gracz1Points", gracz1Points);
        outState.putInt("gracz2Points", gracz2Points);
        outState.putBoolean("gracz1Turn", gracz1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        gracz1Points = savedInstanceState.getInt("gracz1Points");
        gracz2Points = savedInstanceState.getInt("gracz2Points");
        gracz1Turn = savedInstanceState.getBoolean("gracz1Turn");
    }
}
