/*
Конструкции разделения работ итерационного типа
*/

#include <iostream>
#include <omp.h>

/*
Написать программу, в которой объявить и присвоить начальные значения целочисленному массиву a[100].
Используя конструкцию parallel for и reduction вычислить средние арифметические значения элементов массива a.
Одно при помощи reduction, другое без. Сравнить полученные значения. Объяснить возможную разность результатов.
*/

void sixth_task()
{
	int a[100];
	for (int i = 0; i < 100; i++)
	{
		a[i] = rand();
	}
	int fsum = 0;
#pragma omp parallel for
	for(int i = 0; i < 100; i++)
	{
		fsum += a[i];
	}
	printf("for sum : %d \n", fsum);
	fsum = 0;
#pragma omp parallel for reduction(+:fsum)
	for (int i = 0; i < 100; i++) {
		{
			fsum += a[i];
		}
	}
	printf("for reduction sum : %d \n", fsum);
	fsum = 0;
	for (int i = 0; i < 100; i++)
	{
		fsum += a[i];
	}
	printf("for sum : %d \n", fsum);
}

/* 
Написать программу, в которой определить две параллельные области, каждая из которых содержит итерационную конструкцию 
for выполняющую инициализацию элементов одномерных массивов целых чисел a[12], b[12] и c[12]. 
Число нитей перед первой областью задать равным 3, перед второй – равным 4. 
Первая параллельная область выполняет инициализацию элементов массивов a и b с использованием статического распределения итераций,
размер порции итераций выбрать самостоятельно, вторая параллельная область выполняет инициализацию элементов массива c по следующему правилу c[i] = a[i] + b[i],
с использованием динамического распределения итераций, размер порции итераций выбрать самостоятельно. 
В каждой области определить и выдать на экран количество нитей, номер нити и результат выполнения цикла.
Убедиться в правильности работы программы.
*/

void seventh_task()
{
	int a[12];
	int b[12];
	int c[12];
#pragma omp parallel for schedule(static, 4) num_threads(3)
	for (int i = 0; i < 12; i++)
	{
		a[i] = rand();
		b[i] = rand();
		printf("a[%d] = %d, b[%d] = %d, thread = %d \n", i, a[i], i, b[i], omp_get_thread_num());
	}
#pragma omp parallel for schedule(dynamic, 3) num_threads(4)
	for (int i = 0; i < 12; i++)
	{
		c[i] = a[i] + b[i];
		printf("c[%d]  = %d, a[%d] = %d, b[%d] = %d, thread = %d \n",i, c[i], i, a[i], i, b[i], omp_get_thread_num());
	}
}

/*
Написать программу, в которой объявлен массив из 16000 элементов и инициализирован так, что значение элемента массива равно его порядковому номеру.
Затем создайте результирующий массив, в котором (за исключением крайних элементов) будут средние значения исходного массива:
b[i] = (a[i-1] + a[i] + a[i+1])/3.0
Запустите программу с 8-ю процессами при различных типах распределения работ (static, dynamic, guided, auto(runtime, если auto не работает)) 
и посмотреть время на разных размерах порций.
*/

void eigth_task_static(long a[], double b[]) {
	double start_time = omp_get_wtime();
#pragma omp parallel for schedule(static, 2000)
	for (int i = 1; i < 15999; i++)
	{
		b[i] = ( a[i - 1] + a[i] + a[i + 1]) / 3.0;
	}
	double end = omp_get_wtime();
	printf("static time = %.16g \n", end - start_time);
}

void eigth_task_dynamic(long a[], double b[]) {
	double start_time = omp_get_wtime();
#pragma omp parallel for schedule(dynamic, 500)
	for (int i = 1; i < 15999; i++)
	{
		b[i] = (a[i - 1] + a[i] + a[i + 1]) / 3.0;
	}
	double end = omp_get_wtime();
	printf("dynamic time = %.16g \n", end - start_time);
}

void eigth_task_guided(long a[], double b[]) {
	double start_time = omp_get_wtime();
#pragma omp parallel for schedule(guided, 500)
	for (int i = 1; i < 15999; i++)
	{
		b[i] = (a[i - 1] + a[i] + a[i + 1]) / 3.0;
	}
	double end = omp_get_wtime();
	printf("guided time = %.16g \n", end - start_time);
}

void eigth_task_runtime(long a[], double b[]) {
	double start_time = omp_get_wtime();
#pragma omp parallel for schedule(runtime)
	for (int i = 1; i < 15999; i++)
	{
		b[i] = (a[i - 1] + a[i] + a[i + 1]) / 3.0;
	}
	double end = omp_get_wtime();
	printf("runtime time = %.16g \n", end - start_time);
}

void eigth_task()
{
	long a[16000];
	double b[16000];
#pragma omp parallel for 
	for (int i = 0; i < 16000; i++) {
		a[i] = i;
	}
	eigth_task_runtime(a, b);
	eigth_task_static(a, b);
	eigth_task_dynamic(a, b);
	eigth_task_guided(a, b);
}


/*
Используя возможности OpenMP написать программу умножения матрицы на вектор. 
Сравнить время выполнения последовательной и параллельных программ (выбрать наилучший schedule). 
Определить размеры матрицы при которых параллельная программа начинает работать быстрей последовательной
*/

void ninth_task()
{
	const int size = 2000;
	int* vector = new int[size];
	int** matrix = new int*[size];
	for (int i = 0; i < size; i++)
	{
		matrix[i] = new int[size];
		vector[i] = rand();
		for (int j = 0; j < size; j++)
		{
			matrix[i][j] = rand();
		}
	}
	double start_time = omp_get_wtime();
	for (int i = 0; i < size; i++)
	{
		for (int j = 0; j < size; j++)
		{
			vector[i] = matrix[i][j] * vector[j];
		}
	}
	double end_time = omp_get_wtime();
	printf("seq time =  %.16g \n", end_time - start_time);
	start_time = omp_get_wtime();
#pragma omp parallel for schedule(dynamic, 63)
	for (int i = 0; i < size; i++)
	{
		for (int j = 0; j < size; j++)
		{
			vector[i] = matrix[i][j] * vector[j];
		}
	}
	end_time = omp_get_wtime();
	printf("par time =  %.16g \n", end_time - start_time);
	delete[] vector;
	for (int i = 0; i < size; i++)
	{
		delete matrix[i];
	}
	delete[] matrix;
}


int main()
{
	ninth_task();
	return EXIT_SUCCESS;
}

