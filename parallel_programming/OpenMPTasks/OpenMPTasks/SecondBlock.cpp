/*
����������� ���������� ����� ������������� ����
*/

#include <iostream>
#include <omp.h>

/*
�������� ���������, � ������� �������� � ��������� ��������� �������� �������������� ������� a[100].
��������� ����������� parallel for � reduction ��������� ������� �������������� �������� ��������� ������� a.
���� ��� ������ reduction, ������ ���. �������� ���������� ��������. ��������� ��������� �������� �����������.
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
�������� ���������, � ������� ���������� ��� ������������ �������, ������ �� ������� �������� ������������ ����������� 
for ����������� ������������� ��������� ���������� �������� ����� ����� a[12], b[12] � c[12]. 
����� ����� ����� ������ �������� ������ ������ 3, ����� ������ � ������ 4. 
������ ������������ ������� ��������� ������������� ��������� �������� a � b � �������������� ������������ ������������� ��������,
������ ������ �������� ������� ��������������, ������ ������������ ������� ��������� ������������� ��������� ������� c �� ���������� ������� c[i] = a[i] + b[i],
� �������������� ������������� ������������� ��������, ������ ������ �������� ������� ��������������. 
� ������ ������� ���������� � ������ �� ����� ���������� �����, ����� ���� � ��������� ���������� �����.
��������� � ������������ ������ ���������.
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
�������� ���������, � ������� �������� ������ �� 16000 ��������� � ��������������� ���, ��� �������� �������� ������� ����� ��� ����������� ������.
����� �������� �������������� ������, � ������� (�� ����������� ������� ���������) ����� ������� �������� ��������� �������:
b[i] = (a[i-1] + a[i] + a[i+1])/3.0
��������� ��������� � 8-� ���������� ��� ��������� ����� ������������� ����� (static, dynamic, guided, auto(runtime, ���� auto �� ��������)) 
� ���������� ����� �� ������ �������� ������.
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
��������� ����������� OpenMP �������� ��������� ��������� ������� �� ������. 
�������� ����� ���������� ���������������� � ������������ �������� (������� ��������� schedule). 
���������� ������� ������� ��� ������� ������������ ��������� �������� �������� ������� ����������������
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

