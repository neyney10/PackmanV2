package GUI.Animation;

import javax.swing.JPanel;

import GUI.JBackground;
import Game.Game;
import Robot.Play;

public class AutoRun extends Thread {

    private JBackground GUI;
    private int refreshDelayRate;
    private Play play;

    public AutoRun(JBackground GUI, int refreshDelayRate) {
        this.GUI = GUI;
        this.refreshDelayRate = refreshDelayRate;
        this.play = GUI.getPlay();
        setDaemon(true);
    }

    @Override
    public void run() {
        double orientation;
        while(this.play.isRuning()) {
            orientation = GUI.getGame().getPlayer().getOrientation();
            // advance game by a single step
            play.rotate(orientation);
            // refresh - repaint panel
            GUI.repaint();
            // total time took to compute
            try {
                Thread.sleep(refreshDelayRate);
                
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }


}