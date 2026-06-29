import numpy as np
import matplotlib.pyplot as plt
from scipy.signal import find_peaks
import json
import os
import logging

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def main():
    input_npy = './inputs/imu_data.npy'
    output_json = './outputs/resultados.json'
    output_img_grafica = './outputs/grafica_ciclos.png'

    if not os.path.exists(input_npy):
        logging.error(f"Archivo no encontrado: {input_npy}")
        return

    # Procesamiento de Señal IMU
    data = np.load(input_npy)
    time_raw = data[:, 0]
    time_sec = (time_raw - time_raw[0]) / 1e9  
    gyro_z = data[:, 6]

    # Filtro Antivibracion
    window = 10
    gyro_z_smooth = np.convolve(gyro_z, np.ones(window)/window, mode='same')

    # Deteccion
    desviacion = np.std(gyro_z_smooth)
    umbral = desviacion * 0.8 
    distancia_minima = 150 

    peaks_pos, _ = find_peaks(gyro_z_smooth, prominence=umbral, distance=distancia_minima, height=np.mean(gyro_z_smooth[gyro_z_smooth > 0])*0.5)
    peaks_neg, _ = find_peaks(-gyro_z_smooth, prominence=umbral, distance=distancia_minima, height=np.mean(-gyro_z_smooth[gyro_z_smooth < 0])*0.5)

    tiempos_pos = np.diff(time_sec[peaks_pos])
    tiempos_neg = np.diff(time_sec[peaks_neg])

    promedio_pos = float(np.mean(tiempos_pos)) if len(tiempos_pos) > 0 else 0.0
    promedio_neg = float(np.mean(tiempos_neg)) if len(tiempos_neg) > 0 else 0.0
    
    lado_optimo = "Izquierdo" if promedio_neg < promedio_pos else "Derecho"
    lado_ineficiente = "Derecho" if lado_optimo == "Izquierdo" else "Izquierdo"
    
    tiempo_optimo = min(promedio_pos, promedio_neg)
    tiempo_lento = max(promedio_pos, promedio_neg)
    varianza = round(tiempo_lento - tiempo_optimo, 2)

    # Parametros de Negocio
    ciclos_diarios = 500  
    pases_por_camion = 6 
    toneladas_por_pase = 100
    dias_mes = 30
    
    # Impacto Diario
    segundos_perdidos_dia = varianza * ciclos_diarios
    horas_dia = round(segundos_perdidos_dia / 3600, 2)
    pases_extra_dia = int(segundos_perdidos_dia / tiempo_optimo) if tiempo_optimo > 0 else 0
    camiones_dia = int(pases_extra_dia / pases_por_camion)
    toneladas_dia = pases_extra_dia * toneladas_por_pase

    # Impacto Mensual
    horas_mes = round(horas_dia * dias_mes, 2)
    camiones_mes = camiones_dia * dias_mes
    toneladas_mes = toneladas_dia * dias_mes

    # Estructura JSON Oficial del Proyecto
    resultados = {
        "1_diagnostico_operativo": {
            "flanco_ineficiente": lado_ineficiente,
            "flanco_optimo": lado_optimo,
            "retraso_por_ciclo_segundos": varianza
        },
        "2_impacto_diario_recuperable": {
            "tiempo_muerto_horas": horas_dia,
            "camiones_no_cargados": camiones_dia,
            "toneladas_perdidas": toneladas_dia
        },
        "3_impacto_mensual_recuperable": {
            "tiempo_muerto_horas": horas_mes,
            "camiones_no_cargados": camiones_mes,
            "toneladas_perdidas": toneladas_mes
        },
        "4_configuracion_escenario": {
            "pases_para_llenado": pases_por_camion,
            "ciclos_evaluados": len(peaks_pos) + len(peaks_neg)
        },
        "5_respuestas_oficiales_reto": {
            "Q1_Que_y_como_midieron": "Medimos la asimetria en tiempos de ciclo por flanco de carga. Filtramos el ruido mecanico de la señal del giroscopio (eje Z) del IMU y segmentamos algoritmicamente los giros a la izquierda y derecha para comparar promedios.",
            "Q2_Que_dijeron_los_datos": f"Revelaron una ineficiencia silenciosa: el flanco {lado_ineficiente} es {varianza}s mas lento. Proyectado a un mes, esta micro-varianza cuesta {horas_mes} horas operativas y {toneladas_mes} toneladas de mineral no movido.",
            "Q3_Recomendacion_al_operador": "Implementar guias de terreno para estandarizar el aculatamiento (parqueo) de camiones en ambos flancos. Igualar la geometria del flanco ineficiente al optimo recuperara capacidad sin costo de capital (Cero CAPEX).",
            "Q4_Proximos_pasos_con_6_horas_mas": "Desarrollar un pipeline de Edge AI (vision computacional) integrado a las camaras de la pala para medir el angulo de parqueo del camion entrante en tiempo real y alertar al operador si no esta en el radio optimo."
        }
    }

    with open(output_json, 'w', encoding='utf-8') as f:
        json.dump(resultados, f, indent=4, ensure_ascii=False)

    plt.figure(figsize=(12, 5))
    plt.plot(time_sec, gyro_z_smooth, color='#2c3e50', label='Velocidad Angular (Yaw)', alpha=0.9)
    plt.plot(time_sec[peaks_pos], gyro_z_smooth[peaks_pos], "x", color='#e74c3c', markersize=7, label='Ciclo Flanco Derecho')
    plt.plot(time_sec[peaks_neg], gyro_z_smooth[peaks_neg], "o", color='#27ae60', markersize=6, label='Ciclo Flanco Izquierdo')
    plt.axhline(0, color='black', linestyle='--', linewidth=0.5)
    plt.title(f'Auditoria de Ciclos: Ineficiencia detectada en Flanco {lado_ineficiente}')
    plt.xlabel('Tiempo de Operacion (Segundos)')
    plt.ylabel('Yaw (Grados/s)')
    plt.legend()
    plt.grid(True, alpha=0.3)
    plt.tight_layout()
    plt.savefig(output_img_grafica, dpi=300)
    plt.close()

if __name__ == "__main__":
    main()