import pandas as pd
from collections import defaultdict
import numpy as np
from sklearn.ensemble import RandomForestClassifier
from datetime import datetime, timedelta

def carregar_dados_2025():
    """Dados fictícios atualizados para 2025"""
    return pd.DataFrame({
        'Concurso': [3120, 3119, 3118, 3117, 3116, 3115, 3114],
        'Data': ['2025-08-16', '2025-08-13', '2025-08-09', '2025-08-06', 
                '2025-08-02', '2025-07-30', '2025-07-26'],
        'N1': [7, 12, 4, 9, 15, 3, 11],
        'N2': [22, 18, 25, 20, 17, 24, 19],
        'N3': [31, 35, 33, 28, 36, 30, 34],
        'N4': [42, 45, 41, 47, 43, 46, 40],
        'N5': [50, 53, 55, 51, 57, 52, 54],
        'N6': [60, 58, 59, 56, 60, 57, 55]
    })

# Carregar dados atualizados
print("Carregando dados atualizados para 2025...")
dados = carregar_dados_2025()

# Análise de frequência (código mantido igual)
numeros = dados[['N1','N2','N3','N4','N5','N6']].values.flatten()
freq = pd.Series(numeros).value_counts().sort_index()

# Calcular atrasos (código mantido igual)
atrasos = defaultdict(int)
for num in range(1, 61):
    mask = (dados == num).any(axis=1)
    if mask.any():
        atrasos[num] = len(dados) - mask[::-1].idxmax()
    else:
        atrasos[num] = len(dados)

# Preparar dados para ML (código mantido igual)
X = [[
    freq.get(num, 0),  # Frequência
    atrasos[num],      # Atraso
    num % 2,           # Ímpar
    num > 30,          # > 30
    num % 10           # Terminação
] for num in range(1, 61)]

y = [1 if num in dados.iloc[-1, 2:8].values else 0 for num in range(1, 61)]

# Modelo de ML (código mantido igual)
model = RandomForestClassifier(n_estimators=100, random_state=42)
model.fit(X, y)

# Gerar palpite (código mantido igual)
probabilidades = model.predict_proba(X)[:,1]
palpite = sorted(range(1, 61), key=lambda x: (-probabilidades[x-1], -freq.get(x, 0)))[:6]

# Exibir resultados atualizados
print("\n=== Últimos Concursos 2025 ===")
print(dados[['Concurso', 'Data', 'N1', 'N2', 'N3', 'N4', 'N5', 'N6']].head(3))

print("\n=== Palpite para o Próximo Concurso (Agosto/2025) ===")
print(f"Recomendação: {sorted(palpite)}")

print("\n=== Estatísticas Atualizadas ===")
print(f"Número mais sorteado recentemente: {freq.idxmax()} ({freq.max()} vezes)")
print(f"Número mais atrasado: {max(atrasos.items(), key=lambda x: x[1])[0]} ({max(atrasos.values())} concursos)")
print(f"Distribuição: {len([n for n in palpite if n <= 30])} baixos e {len([n for n in palpite if n > 30])} altos")
print(f"Pares/Ímpares: {len([n for n in palpite if n % 2 == 0])} pares e {6 - len([n for n in palpite if n % 2 == 0])} ímpares")

print("\nObservação: Estes são dados simulados para exercício acadêmico.")
print("Para dados reais, baixe o histórico atualizado no site da Caixa.")