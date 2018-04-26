package EVcharge;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class Chart extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

    public Chart(final String title,ChPoint[] chPoint) {

        super(title);

        IntervalCategoryDataset dataset = createSampleDataset(chPoint);

        final JFreeChart chart = ChartFactory.createGanttChart(
            "EV Charging",  
            "Charg Points",            
            "Time",            
            dataset,           
            true,              
            true,                
            false               
        );
        final CategoryPlot plot = (CategoryPlot) chart.getPlot();  
        plot.getRangeAxis().setLowerBound(18000000);
        plot.getRangeAxis().setUpperBound(104400000);
        plot.setRenderer(new MyRenderer());

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1600, chPoint.length*50+100));
        setContentPane(chartPanel);
    }

    private static Date time(Long po) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(po);;
        final Date result = calendar.getTime();
        return result;

    }

    private IntervalCategoryDataset createSampleDataset(ChPoint[] chPoint) {

    	TaskSeries s1 = new TaskSeries("EV Users");

    	Task t = null;
    			
    	for (int i=0;i<chPoint.length;i++){
    		if (chPoint[i]==null) continue;
    		Task t0 = new Task(
        			"Point "+chPoint[i].getID(), time(18000000L),time(18000000L));
    		t = new Task(
    			"Point "+chPoint[i].getID(), time(18000000L),time(104400000L));
    		boolean startFlag = false;
    		int stime=0,etime;
    		int userID=0;
    		for (int j=0;j<1440;j++){
    			if (!startFlag){
    				if (chPoint[i].getTimeSlot(j)>0){
    					startFlag=true;
    					stime = j;
    					userID = chPoint[i].getTimeSlot(j);
    				}
    			}
    			else{
    				if (chPoint[i].getTimeSlot(j)!=userID){
    					etime = j;
    					t.addSubtask(new Task("EVuser "+userID, time((5L*60+stime)*60000), time((5L*60+etime)*60000)));
    					if (chPoint[i].getTimeSlot(j)>0){
    						stime=j;
    						userID=chPoint[i].getTimeSlot(j);
    					}
    					else {
    						startFlag=false;
    					}
    				}   				
    			}
    		}
    		if (startFlag){
				etime = 1440;
				t.addSubtask(new Task("EVuser "+userID, time((5L*60+stime)*60000), time((5L*60+etime)*60000)));
    		}
    		if (t.getSubtaskCount()>0) s1.add(t);
    		else s1.add(t0);
    	}
    	final TaskSeriesCollection collection = new TaskSeriesCollection();
    	collection.add(s1);
    	return collection;
    }


    private static class MyRenderer extends GanttRenderer {

		private static final long serialVersionUID = 1L;
		private static final int PASS = 2; 
        private final List<Color> clut = new ArrayList<Color>();

        private int row;
        private int col;
        private int index;

        public MyRenderer() {
            clut.add(Color.RED);
            clut.add(Color.GREEN);
        }

        @Override
        public Paint getItemPaint(int row, int col) {
            if (this.row != row || this.col != col) {
                this.row = row;
                this.col = col;
                index = 0;
            }
            int clutIndex =(index++/PASS) % 2;
            return clut.get(clutIndex);
        }
    }
}
