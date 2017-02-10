package fjs.jde.method;

import java.applet.*;  
import java.awt.*;  
class thing  
{  
  public static int thingcount = 0;  
  public static int thingfinal = 0;  
  public thing()  
  {  
    ++thingcount;  
  }  
  protected void finalize()  
  {  
    ++thingfinal;  
  }  
}  
public class Final_things extends Applet  
{  
  public Final_things()  
  {  
  }  
  public String getAppletInfo()  
  {  
    return "Name: final_thing/r/n" +  
           "Author: Tim Gooch/r/n" +  
           "Created with Microsoft " +  
           "Visual J++ Version 1.1";  
  }  
  public void init()  
  {  
      resize(320, 240);  
  }  
  public void destroy()  
  {  
  }  
  public void paint(Graphics g)  
  {  
    g.drawString("Created with Microsoft" +  
      "Visual J++ Version 1.1", 10, 20);  
  }  
  public void start()  
  {  
    while(thing.thingfinal < 1)  
    {  
      new thing();  
    }  
  }  
  public void stop()  
  {  
    // System.gc();  
    System.out.println(thing.thingcount +  
      " things constructed");  
    System.out.println(thing.thingfinal +  
      " things finalized");  
  }  
}  
