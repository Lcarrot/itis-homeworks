/*
Общие и защищенные переменные. Разграничения доступа к переменным, конструкции разделения работ не итерационного типа.
*/

#include <iostream>
#include <omp.h>


/*
* Написать программу где каждый поток печатает свой идентификатор, количество потоков всего и строчку «Hello World». Запустить программу с 8 потоками. Всегда ли вывод идентичен? Почему?
*/

void first_task() {
#pragma omp parallel num_threads(8)
	{
		int count = omp_get_num_threads();
		int current_thread_num = omp_get_thread_num();
		printf("Hello world! current - %d, all - %d \n", current_thread_num, count);
	}
}

/*
Написать программу, в которой определить две параллельные области,
выполнение которых зависит от условного оператора #pragma omp parallel if(…),
если заданное значение числа нитей больше 2, область выполняется параллельно, иначе не параллельно.
Число нитей перед первой областью задать равным 3, перед второй – равным 2.
Внутри параллельных областей определить количество нитей и номер каждой нити, результат выдать на экран. Убедиться в правильности работы программы.
*/

void second_task() {
	const int min = 2;
	int threads_count = 3;
#pragma omp parallel num_threads(threads_count) if(threads_count > min)
		{
			printf("first, %d out of %d \n", omp_get_thread_num(), omp_get_num_threads());
		}
		threads_count = 2;
#pragma omp parallel num_threads(threads_count) if (threads_count > min)
		{
			printf("second, %d out of %d \n", omp_get_thread_num(), omp_get_num_threads());
		}
}

/*
Написать программу, в которой объявить и присвоить начальные значения целочисленным переменным: a и b,
определить две параллельные области, первая – с числом нитей 2, вторая – с числом нитей 4. 
Для первой области переменные a и b объявить защищенными, с режимом доступа private и firstprivate соответственно,
внутри области определить номер нити и выполнить увеличение значения переменных на эту величину. 
Выдать на экран значения переменных до входа в параллельную область, внутри параллельной области и после выхода из параллельной области.
Для второй области переменную a объявить общей, переменную b объявить защищенной, с режимом доступа private,
определить номер нити и выполнить уменьшение значения переменных на эту величину. 
Выдать на экран значения переменных до входа в параллельную область, внутри параллельной области и после выхода из параллельной области.
Неинициализированные переменные инициализировать.
*/

void third_task() {
	int a = 0;
	int b = 0;
	printf("before first block a - %d, b - %d \n", a, b);
#pragma omp parallel num_threads(2) private(a) firstprivate(b)
	{
		int current_thread_num = omp_get_thread_num();
		a = 0;
		a += current_thread_num;
		b += current_thread_num;
		printf("current_thread_num - %d, a - %d, b - %d \n", current_thread_num, a, b);
	}
	printf("after first block a - %d, b - %d \n", a, b);
	printf("before second block a - %d, b - %d \n", a, b);
#pragma omp parallel num_threads(4) shared(a) private(b)
	{
		b = 4;
		int current_thread_num = omp_get_thread_num();
		a -= current_thread_num;
		b -= current_thread_num;
		printf("current_thread_num - %d, a - %d, b - %d \n", current_thread_num, a, b);
	}
	printf("after second block a - %d, b - %d \n", a, b);
}

/*
Написать программу, в которой объявить и присвоить начальные значения целочисленным массивам a[10] и b[10],
определить параллельную область, количество нитей задать равным 2, выделить код для основной (номер 0) и нити с номером 1.
Основная нить (master) должна выполнять поиск min значения элементов массива a, нить с номером 1 - поиск max значения элементов массива b. Результат выдать на экран.
*/

void fouth_task()
{
	int a[10];
	int b[10];
	for (int i = 0; i < 10; i++) 
	{
		a[i] = rand();
		b[i] = rand();
	}
#pragma omp parallel num_threads(2)
	{
#pragma omp master
		{
			int min = a[0];
			for (int i = 1; i < 10; i++)
			{
				if (a[i] < min) min = a[i];
			}
			printf("min a = %d, main thread = %d \n", min, omp_get_thread_num());
		}
#pragma omp single 
		{
			int max = b[0];
			for (int i = 1; i < 10; i++)
			{
				if (b[i] > max) max = a[i];
			}
			printf("max b = %d, slave thread = %d \n", max, omp_get_thread_num());
		}
	}
}
/*
Написать программу, в которой объявить и присвоить начальные значения элементам двумерного массива d[6][8], 
для инициализации значений использовать генератор случайных чисел. Используя конструкцию директивы sections…section определить три секции для выполнения следующих операций:
 первая секция выполняет вычисление среднего арифметического значения элементов двумерного массива,
 вторая секция выполняет вычисление минимального и максимального значений элементов двумерного массива,
 третья секция выполняет вычисление количества элементов массива, числовые значения которых кратны 3.
В каждой секции определить и выдать на экран номер исполняющей нити и результат выполнения вычислений.
*/

void fifth_task()
{
	int d[6][8];
	for (int i = 0; i < 6; i++) {
		for (int j = 0; j < 8; j++) {
			d[i][j] = rand();
 		}
	}
#pragma omp parallel sections num_threads(3)
	{
#pragma omp section 
		{
			int sum = 0;
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 8; j++) {
					sum += d[i][j];
				}
			}
			printf("first section: %d, thread_num = %d \n", sum / (6 * 8), omp_get_thread_num());
		}
#pragma omp section 
		{
			int min = d[0][0];
			int max = d[0][0];
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 8; j++) {
					if (d[i][j] > max) max = d[i][j];
					if (d[i][j] < min) min = d[i][j];
				}
			}
			printf("min = %d, max = %d, thread_num = %d \n", min, max, omp_get_thread_num());
		}
#pragma omp section 
		{
			int count = 0;
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 8; j++) {
					if (d[i][j] % 3 == 0) count++;
				}
			}
			printf("devide by 3 count = %d, thread - %d \n", count, omp_get_thread_num());
		}
	}
}


//int main()
//{
//	fifth_task();
//	return EXIT_SUCCESS;
//}

