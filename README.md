# CircleRectImageView


![alt text](https://raw.githubusercontent.com/vickyKDV/CircleRectImageView/vickyKDV-imG1/Screen%20Shot%202019-11-03%20at%2021.08.57.png)

   Cara implementasi
   
   
   Set pada build.gradle application
   
     allprojects {
          repositories {
             ...
             ...
             maven { url "https://jitpack.io" }

          }
      }
    
   Set pada build.gradle module
    
    dependencies {
        ...
        ...
        implementation 'com.github.vickykdv:CircleRectImageView:1.0'
    }
    
    
  Membuat Circle Image pada XML ( Ketika roundAll diisi value maka otomatis akan menjadi Circel Image )
  
     <com.vickykdv.circlerectimageview.CircleRectImage
        android:id="@+id/circleRectImage5"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_gravity="center_horizontal"
        android:layout_margin="14dp"
        android:scaleType="centerCrop"
        android:src="@drawable/img"
        app:border_color="@android:color/holo_orange_light"
        app:border_width="10"
        app:roundAll="500" />

  Membuat Top Left Round dan Bottom Right Round
  
        <com.vickykdv.circlerectimageview.CircleRectImage
            android:id="@+id/circleRectImage2"
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_gravity="center_horizontal"
            android:layout_margin="14dp"
            android:scaleType="centerCrop"
            android:src="@drawable/img"
            app:border_color="@android:color/holo_orange_light"
            app:border_width="10"
            app:roundBottomLeft="100"
            app:roundTopRight="100" />
            
            
   Membuat Top Right Round dan Bottom Left Round   
   
        <com.vickykdv.circlerectimageview.CircleRectImage
            android:id="@+id/circleRectImage2"
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_gravity="center_horizontal"
            android:layout_margin="14dp"
            android:scaleType="centerCrop"
            android:src="@drawable/img"
            app:border_color="@android:color/holo_orange_light"
            app:border_width="10"
            app:roundBottomLeft="100"
            app:roundTopRight="100" />
            
            
   Membuat Rectangle sama rata
   
         <com.vickykdv.circlerectimageview.CircleRectImage
             android:id="@+id/circleRectImage4"
             android:layout_width="100dp"
             android:layout_height="100dp"
             android:layout_gravity="center_horizontal"
             android:layout_margin="14dp"
             android:scaleType="centerCrop"
             android:src="@drawable/img"
             app:border_color="@android:color/holo_orange_light"
             app:border_width="10"
             app:roundBottomLeft="50"
             app:roundBottomRight="50"
             app:roundTopLeft="50"
             app:roundTopRight="50" />


