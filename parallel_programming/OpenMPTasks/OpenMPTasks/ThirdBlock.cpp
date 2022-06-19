/*
�������� ������������� �����
*/

#include <iostream>
#include <omp.h>
#include <windows.h>

/*
�������� ���������, � ������� �������� � ��������� ��������� �������� ��������� ���������� ������� d[6][8], ��� ������������� �������� ������������ ��������� ��������� �����. 
��������� ����������� ��������� omp parallel for � omp critical ���������� ����������� � ������������ �������� ��������� ���������� �������. ����������� ������ reduction ��� max � min. 
���������� ����� ������ ��������������. ��������� ������ �� �����.
*/

void tenth_task()
{
	int d[6][8];
	for (int i = 0; i < 6; i++)
	{
		for (int j = 0; j < 8; j++)
		{
			d[i][j] = rand();
		}
	}
	int max = d[0][0];
	int min = d[0][0];
#pragma omp parallel for
	for (int i = 0; i < 6; i++)
	{
		for (int j = 0; j < 8; j++)
		{
			if (d[i][j] >= min && d[i][j] <= max) {
				continue;
			}
#pragma omp critical 
			{
				if (d[i][j] < min) min = d[i][j];
				if (d[i][j] > max) max = d[i][j];
			}
		}
	}
	printf("max = %d, min = %d \n", min, max);
}

/*
�������� ���������, � ������� �������� � ��������� ��������� �������� ������� ����� ����� a[30], ��� ������������� �������� ������������ ��������� ��������� �����. 
��������� ����������� omp parallel for � omp atomic ��������� ���������� ��������� �������, �������� �������� ������� ������ 9. ���������� ����� ������ ��������������. 
��������� ������ �� �����.
*/

void eleventh_task()
{
	int a[30];
	int count = 0;
	for (int i = 0; i < 30; i++)
	{
		a[i] = rand();
	}
#pragma omp parallel for num_threads(8)
	for (int i = 0; i < 30; i++)
	{
		if (a[i] % 9 == 0)
		{
#pragma omp atomic 
			count++;
		}
	}
	printf("divided by 9 count = %d", count);
}

/*
�������� ���������, � �������, �������� � ��������� ���������� ���������� ������ ����� �����. ��������� ����������� OpenMP ����� ������������ �������� ��������� ������� ������� 7. 
����� ������� � ���������� ������� ���������� ��������������. ��������� ������ �� �����. ��� ������������� �������� �������� ��������� ������������ �������� ����������� ������. (2 �����)
*/

void twelveth_task()
{
	int a[30];
	for (int i = 0; i < 30; i++)
	{
		a[i] = rand();
	}
	int max = INT16_MIN;
#pragma omp parallel for
	for (int i = 0; i < 30; i++)
	{
		if (a[i] % 7 == 0)
		{
#pragma omp critical
			{
				if (max < a[i]) max = a[i];
			}
		}
	}
	printf("max divided by 7 = %d", max);
}

/*
������������� ������ 1 ���, ����� ������ ������������� ���� �������������� � �������� (���������) �������. ���������� ��� ������� 5 �������� �������.
������������ ����� ��� ����� ������. (������ ������� 1 ���� � max 5 ������)
*/

void thirteenth_task_1()
{
	int current_thread = 7;
#pragma omp parallel num_threads(8)
	{
		bool isDone = false;
		while (!isDone)
		{
#pragma omp critical 
			{
				if (omp_get_thread_num() == current_thread)
				{
					current_thread--;
				}
			}
			printf("thread = %d \n", omp_get_thread_num());
			isDone = true;
		}
	}
}

void thirteenth_task_2()
{
#pragma omp parallel num_threads(8)
	{
		Sleep(1000 / (omp_get_thread_num() + 1));
		printf("Thread %d \n", omp_get_thread_num());
	}
}

void thirteenth_task_3()
{
#pragma omp parallel
	{
		int nthreads = omp_get_num_threads();
		for (int i = nthreads - 1; i >= 0; i--)
		{
#pragma omp barrier
			{
				if (i == omp_get_thread_num())
				{
					printf("thread = %d \n", omp_get_thread_num());
				}
			}
		}
	}
}

void thirteenth_task_4()
{
	#pragma omp parallel for schedule(static, 1)
		for (int i = omp_get_num_threads() - 1; i >=0; i--)
		{
			Sleep(100*i);
			printf("thread = %d \n", omp_get_thread_num());
		}
}

int thirteenth_task_5() {
	omp_lock_t my_lock;
	omp_init_lock(&my_lock);
	int current_thread = 7;
#pragma omp parallel
	{
		while (current_thread > 0)
		{
			omp_set_lock(&my_lock);
			if (current_thread == omp_get_thread_num()) {
				printf("thread = %d \n", omp_get_thread_num());
				current_thread--;
			}
			omp_unset_lock(&my_lock);
		}
	}
	omp_destroy_lock(&my_lock);
}

int main()
{
	thirteenth_task_5();
	return EXIT_SUCCESS;
}
