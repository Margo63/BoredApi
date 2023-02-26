package com.example.boredapi

import android.content.Context
import android.hardware.*
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.getSystemService
import com.example.boredapi.ui.theme.BoredApiTheme
import java.lang.Math.abs

class MainActivity : ComponentActivity(),SensorEventListener {
    val mainViewModel :MainViewModel by viewModels()

    private lateinit var sensorManager: SensorManager

    private var rotationX by mutableStateOf(0.0f)
    private var rotationY by mutableStateOf(0.0f)
    private var rotation by mutableStateOf(0.0f)
    private var translationX by mutableStateOf(0.0f)
    private var translationY by mutableStateOf(0.0f)
    private var counter by mutableStateOf(0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//по документации
//        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        val mSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION)
//        val triggerEventListener = object:TriggerEventListener(){
//            override fun onTrigger(p0: TriggerEvent?) {
//                Log.d("MYSENSOR","pos changed")
//            }
//        }
        setUpSensorStuff()
        setContent {
            BoredApiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(text = mainViewModel.activity)

                        //старый вызов по кнопке
//                        Button(onClick = {
//                            mainViewModel.getActivity()
//
//                        }) {
//                            Text("click")
//
//                        }
                    }


                }
            }
        }
    }

    private fun setUpSensorStuff(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(this,it,SensorManager.SENSOR_DELAY_NORMAL,SensorManager.SENSOR_DELAY_NORMAL)//можно скорость настроить
        }
    }

    //имплементированая функция
    override fun onSensorChanged(p0: SensorEvent?) {
        if(p0?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            val sides = p0.values[0]
            val upDown = p0.values[1]


            if(abs(rotationX - upDown*3f)>2.5 || abs(rotationY - sides * 3f)>2.5){
                counter++
                Log.d("MYSENSOR","changed")
            }
            //если 10 раз положение изменилось, меняется надпись
            if(counter>=10){
                counter = 0
                mainViewModel.getActivity()
            }

            //фиксация положения

            rotationX = upDown * 3f
            rotationY = sides * 3f
            rotation = - sides
            translationX = sides * -10
            translationY = upDown * 10



            //фиксация нейтрального положения
//            if(upDown.toInt()==0 && sides.toInt() == 0){
//                Log.d("MYSENSOR","GREEN")
//            }else{
//                Log.d("MYSENSOR","RED")
//            }

        }

    }
    //имплементированая функция
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    //родительский?
    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}
