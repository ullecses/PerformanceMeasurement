# Reflection Benchmarking

Этот репозиторий содержит бенчмарки, которые сравнивают производительность различных методов доступа к данным в Java:

- Прямой доступ к полям
- Доступ через рефлексию
- Использование `MethodHandles`
- Использование `LambdaMetafactory`

## Результаты бенчмарков

Ниже приведены результаты для различных методов доступа к данным, измеренные в наносекундах на операцию (ns/op) в режиме **AverageTime**.

| Benchmark                              | Mode  | Cnt | Score  | Error  | Units   |
|----------------------------------------|-------|-----|--------|--------|---------|
| ReflectionBenchmark.directAccess       | avgt  | 15  | 0,600  | ± 0,016 | ns/op   |
| ReflectionBenchmark.lambdaMetafactory  | avgt  | 15  | 0,831  | ± 0,011 | ns/op   |
| ReflectionBenchmark.methodHandles      | avgt  | 15  | 3,275  | ± 0,026 | ns/op   |
| ReflectionBenchmark.reflection         | avgt  | 15  | 5,094  | ± 0,064 | ns/op   |

## Описание методов

1. **directAccess**  
   Прямой доступ к полям через вызов метода `name()`.

2. **reflection**  
   Использование рефлексии для вызова метода `name()` через объект `Method`.

3. **methodHandles**  
   Использование `MethodHandle` для вызова метода `name()`.

4. **lambdaMetafactory**  
   Создание лямбды с использованием `LambdaMetafactory` и вызов метода через интерфейс.

## Параметры бенчмарков

- **JMH версия**: 1.37
- **JVM версия**: OpenJDK 22.0.2, 64-бит
- **Количество потоков**: 1
- **Количество запусков**:
  - **Warmup**: 3 итерации по 7 секунд
  - **Measurement**: 5 итераций по 7 секунд
