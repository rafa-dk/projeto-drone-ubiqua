package com.google.mediapipe.examples.gesturerecognizer

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class DroneControlAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Opcional: tratar eventos de mudança de janela aqui
    }

    override fun onInterrupt() {
        // Chamado quando o sistema interrompe o serviço
    }

    // Simula toque
    fun clickAt(x: Float, y: Float) {
        val path = Path()
        path.moveTo(x, y)
        val builder = GestureDescription.Builder()
        builder.addStroke(GestureDescription.StrokeDescription(path, 0, 100))

        // Adicionando um callback para monitorar o status
        dispatchGesture(builder.build(), object : AccessibilityService.GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                Log.d("DRONE_CONTROL", "TOQUE EXECUTADO COM SUCESSO EM: $x, $y")
            }

            override fun onCancelled(gestureDescription: GestureDescription?) {
                super.onCancelled(gestureDescription)
                Log.e("DRONE_CONTROL", "TOQUE CANCELADO! Verifique se a tela está ligada ou se há sobreposições.")
            }
        }, null)
    }

    // Simula toque com atraso
    fun clickAt(x: Float, y: Float, delayMs: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            clickAt(x, y)
        }, delayMs)
    }

    // Singleton para permitir que o GestureRecognizerHelper acesse o serviço
    companion object {
        var instance: DroneControlAccessibilityService? = null
    }

    // Chamado quando o usuário ativa o serviço nas configurações do Android
    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    // Limpa a instância quando o serviço é desativado
    override fun onUnbind(intent: android.content.Intent?): Boolean {
        instance = null
        return super.onUnbind(intent)
    }
}