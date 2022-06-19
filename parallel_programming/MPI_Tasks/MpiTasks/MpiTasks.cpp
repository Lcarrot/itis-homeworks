// MpiTasks.cpp : Этот файл содержит функцию "main". Здесь начинается и заканчивается выполнение программы.
//

#include <iostream>
#include <mpi.h>

int rank, size;
const int array_size = 10000;

void fillArray(int* array) {
	for (int i = 0; i < array_size; i++) {
		array[i] = rand();
	}
}

void first_task() {
	printf("Hello, world! size = %d, rank = %d \n", size, rank);
}

void second_task() {
	int array[array_size];
	if (rank == 0) {
		fillArray(array);
	}
	int differnce = array_size / size;
	int start = rank * differnce;
	int end = differnce * (rank + 1);
	if (rank == size - 1) {
		end = size;
	}
	MPI_Bcast(&array, array_size, MPI_INT, 0, MPI_COMM_WORLD);
	int max = array[start];
	for (int i = start + 1; i < end; i++) {
		if (array[i] > max) max = array[i];
	}
	int abs_max;
	MPI_Reduce(&max, &abs_max, 1, MPI_INT, MPI_MAX, 0, MPI_COMM_WORLD);
	printf("local_max = %d, rank = %d \n", max, rank);
	if (rank == 0) {
		printf("max = %d \n", abs_max);
	}
}

void third_task() {
	int radius = 100;
	int square_side = radius * 2;
	int point_in_circle_one_rank = 0;
	int iteration_for_one_rank = 1e7;
	for (int i = 0; i < iteration_for_one_rank; i++) {
		int x = rand() % square_side - square_side / 2;
		int y = rand() % square_side - square_side / 2;
		if ((x * x + y * y) <= radius * radius)
			point_in_circle_one_rank++;
	}
	int all_points_in_circle = 0;
	MPI_Reduce(&point_in_circle_one_rank, &all_points_in_circle, 1, MPI_INT, MPI_SUM, 0, MPI_COMM_WORLD);
	if (rank == 0) {
		double pi = 4.0 * all_points_in_circle / ((double)iteration_for_one_rank * size);
		printf("pi = %.6f \n", pi);
	}
}

void fourth_task() {
	int offset = array_size / size;
	int buffer_size = offset;
	int* buffer = new int[buffer_size];
	int* scounts = new int[size];
	int* displs = new int[size];
	int array[array_size];
	for (int i = 0; i < size - 1; i++) {
		scounts[i] = offset;
		displs[i] = i * offset;
	}
	scounts[size - 1] = offset + array_size % size;
	displs[size - 1] = (size - 1) * array_size / size;
	if (rank == 0) {
		for (int i = 0; i < array_size; i++) {
			array[i] = rand() % 100;
		}
	}
	for (int i = 0; i < size; i++) {
		if (rank == i) {
			buffer = new int[scounts[i]];
			break;
		}
	}
	MPI_Scatterv(array, scounts, displs, MPI_INT, buffer, buffer_size, MPI_INT, 0, MPI_COMM_WORLD);
	int local_count = 0;
	int local_sum = 0;
	for (int i = 0; i < buffer_size; i++) {
		if (buffer[i] > 0) {
			local_sum += buffer[i];
			local_count++;
		}
	}
	int time_buffer[2] = { local_sum, local_count };
	const int new_buf_size = 2 * size;
	int* new_buffer = new int[new_buf_size];
	MPI_Gather(&time_buffer[0], 2, MPI_INT, &new_buffer[0], 2, MPI_INT, 0, MPI_COMM_WORLD);
	if (rank == 0) {
		int sum = 0;
		int count = 0;
		for (int i = 0; i < new_buf_size; i++) {
			if (i % 2 == 0) {
				sum += new_buffer[i];
			}
			else {
				count += new_buffer[i];
			}
		}
		printf("average = %d \n", sum / count);
	}
}

void fifth_task() {
	const int matrix_size = array_size * array_size;
	int offset = matrix_size / size;
	int* scounts = new int[size];
	int* displs = new int[size];
	for (int i = 0; i < size - 1; i++) {
		scounts[i] = offset;
		displs[i] = i * offset;
	}
	scounts[size - 1] = offset + matrix_size % size;
	displs[size - 1] = (size - 1) * matrix_size % size;
	int* matrixA = new int[array_size * array_size];
	int* matrixB = new int[array_size * array_size];
	int* buffer_matrixA = new int[matrix_size / size];
	int* buffer_matrixB = new int[matrix_size / size];
	long* time_c = new long[matrix_size / size];
	if (rank == 0) {
		matrixA = new int[array_size * array_size];
		matrixB = new int[array_size * array_size];
		for (int i = 0; i < array_size; i++) {
			for (int k = 0; k < array_size; k++) {
				matrixA[i * array_size + k] = rand() % 100;
				matrixB[k * array_size + i] = rand() % 100;
			}
		}
	}

	for (int i = 0; i < size; i++) {

	}
	MPI_Scatterv(matrixA, scounts, displs, MPI_INT, buffer_matrixA, matrix_size / size, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Scatterv(matrixB, scounts, displs, MPI_INT, buffer_matrixB, matrix_size / size, MPI_INT, 0, MPI_COMM_WORLD);
	for (int i = 0; i < array_size / size; i++) {
		for (int k = 0; k < array_size; k++) {
			time_c[i * array_size + k] = 0;
			for (int t = 0; t < array_size; t++) {
				int c_index = i * array_size + k;
				int a_index = i * array_size + t;
				int b_index = k * array_size + t;
				time_c[c_index] += (buffer_matrixA[a_index] * buffer_matrixB[b_index]) % 100;
				/*printf("a[%d][%d] = %d, b[%d][%d] = %d, c[%d][%d] = %d \n", i, t, buffer_matrixA[a_index], t, k, buffer_matrixB[b_index], i, k, time_c[c_index]);*/
			}
		}
	}
	long* c = new long[matrix_size];
	MPI_Gather(time_c, matrix_size / size, MPI_LONG, c, matrix_size / size, MPI_LONG, 0, MPI_COMM_WORLD);
	if (rank == 0) {
		for (int i = 0; i < array_size; i++) {
			for (int k = 0; k < array_size; k++) {
				printf("%d ", c[i * array_size + k]);
			}
			printf(" \n");
		}
	}
}

void sixth_task() {
	const int matrix = array_size * array_size;
	const int offset = matrix / size;
	int buffer_size = offset;
	int* buffer = new int[buffer_size];
	int* scounts = new int[size];
	int* displs = new int[size];
	int* array = new int[matrix];
	for (int i = 0; i < size - 1; i++) {
		scounts[i] = offset;
		displs[i] = i * offset;
	}
	scounts[size - 1] = offset + array_size % size;
	displs[size - 1] = (size - 1) * array_size / size;
	if (rank == 0) {
		for (int i = 0; i < matrix; i++) {
			array[i] = rand() % 100;
		}
	}
	for (int i = 0; i < size; i++) {
		if (rank == i) {
			buffer = new int[scounts[i]];
			break;
		}
	}
	MPI_Scatterv(array, scounts, displs, MPI_INT, buffer, buffer_size, MPI_INT, 0, MPI_COMM_WORLD);
	int minmax = buffer[0];
	int maxmin = buffer[0];
	for (int i = 0; i < array_size / size; i++) {
		int min = buffer[i * array_size];
		int max = buffer[i * array_size];
		for (int k = 0; k < array_size; k++) {
			if (min > buffer[i * array_size + k]) min = buffer[i * array_size + k];
			if (max < buffer[i * array_size + k]) max = buffer[i * array_size + k];
		}
		if (maxmin < min) maxmin = min;
		if (minmax > max) minmax = max;
	}
	int time_buffer[2] = { maxmin, minmax };
	int* min_max = new int[2 * size];
	MPI_Gather(time_buffer, 2, MPI_INT, min_max, 2, MPI_INT, 0, MPI_COMM_WORLD);
	maxmin = min_max[0];
	minmax = min_max[1];
	if (rank == 0) {
		for (int i = 0; i < 2 * size; i++) {
			if (i % 2 == 0 && maxmin < buffer[i]) maxmin = buffer[i];
			if (i % 2 != 0 && minmax > buffer[i]) minmax = buffer[i];
		}
		printf("maxmin = %d, minmax = %d \n", maxmin, minmax);
	}
}

void seventh_task() {
	const int matrix = array_size * array_size;
	const int offset = matrix / size;
	int buffer_size = offset;
	int* buffer_matrix = new int[buffer_size];
	int* buffer_vector = new int[array_size / size];
	int* vector = new int[array_size];
	int* array = new int[matrix];
	if (rank == 0) {
		for (int i = 0; i < array_size; i++) {
			vector[i] = rand() % 100;
			for (int k = 0; k < array_size; k++) {
				array[k * array_size + i] = rand() % 100;
			}
		}
	}
	MPI_Scatter(array, offset, MPI_INT, buffer_matrix, offset, MPI_INT, 0, MPI_COMM_WORLD);
	MPI_Scatter(vector, array_size / size, MPI_INT, buffer_vector, array_size / size, MPI_INT, 0, MPI_COMM_WORLD);
	for (int i = 0; i < array_size; i++) {
		vector[i] = 0;
		for (int k = 0; k < array_size / size; k++) {
			vector[i] += (buffer_matrix[k * array_size + i] * buffer_vector[i]) % 100;
			vector[i] %= 100;
		}
	}
	int* result_vector = new int[array_size];
	int* buffer_vector_result = new int[array_size * size];
	MPI_Gather(vector, array_size, MPI_INT, buffer_vector_result, array_size, MPI_INT, 0, MPI_COMM_WORLD);
	if (rank == 0) {
		for (int i = 0; i < array_size; i++) {
			for (int k = 0; k < size; k++) {
				result_vector[i] += buffer_vector_result[k * array_size + i];
				result_vector[i] %= 100;
			}
			printf("%d \n", result_vector[i]);
		}
	}
}


void eigth_task() {
	int buffer_size = array_size / size;
	int* buffer = new int[array_size / size];
	MPI_Status status;
	int* array = new int[array_size];
	int* new_array = new int[array_size];
	if (rank == 0) {
		fillArray(array);
		for (int i = 0; i < array_size; i++) {
			printf("a[%d] = %d \n ", i, array[i]);
		}
		for (int i = 1; i < size; i++) {
			MPI_Send(&array[i * buffer_size], buffer_size, MPI_INT, i, 0, MPI_COMM_WORLD);
		}
		for (int i = 0; i < array_size / size; i++) {
			buffer[i] = array[i];
			new_array[i] = buffer[i];
		}
	}
	else {
		MPI_Recv(buffer, buffer_size, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
	}
	MPI_Barrier(MPI_COMM_WORLD);
	for (int i = 0; i < array_size / size; i++) {
		printf("b[%d] = %d, rank = %d \n ", i, buffer[i], rank);
	}
	if (rank == 0) {
		for (int i = 1; i < size; i++) {
			MPI_Recv(buffer, buffer_size, MPI_INT, i, 1, MPI_COMM_WORLD, &status);
			int start_index = i * array_size / size;
			for (int i = start_index; i < start_index + sizeof(buffer); i++) {
				new_array[i] = buffer[i - start_index];
			}
		}
		for (int i = 0; i < array_size; i++) {
			printf("c[%d] = %d \n ", i, new_array[i]);
		}
	}
	else {
		MPI_Send(buffer, buffer_size, MPI_INT, 0, 1, MPI_COMM_WORLD);
	}
}

void ninth_task() {
	int buffer_size = array_size / size;
	int* buffer = new int[buffer_size];
	MPI_Status status;
	if (rank == 0) {
		int* array = new int[array_size];
		fillArray(array);
		for (int i = 0; i < array_size; i++) {
			printf("a[%d] = %d \n", i, array[i]);
		}
		for (int i = 1; i < size; i++) {
			MPI_Send(&array[i * buffer_size], buffer_size, MPI_INT, i, 0, MPI_COMM_WORLD);
		}
		for (int i = 0; i < buffer_size; i++) {
			buffer[i] = array[i];
		}
	}
	else {
		MPI_Recv(buffer, buffer_size, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
	}
	for (int i = 0; i < buffer_size / 2; i++) {
		int c = buffer[i];
		buffer[i] = buffer[buffer_size - i - 1];
		buffer[buffer_size - i - 1] = c;
	}
	if (rank == 0) {
		int* new_array = new int[array_size];
		for (int i = 1; i < size; i++) {
			MPI_Recv(&new_array[array_size - (i + 1) * buffer_size], buffer_size, MPI_INT, i, 1, MPI_COMM_WORLD, &status);
		}
		int start_index = array_size - buffer_size;
		for (int i = 0; i < buffer_size; i++) {
			new_array[start_index + i] = buffer[i];
		}
		for (int i = 0; i < array_size; i++) {
			printf("b[%d] = %d \n", i, new_array[i]);
		}
	}
	else {
		MPI_Send(buffer, buffer_size, MPI_INT, 0, 1, MPI_COMM_WORLD);
	}
}

void tenth_task() {
	int* array = new int[array_size];
	int* bsend_buffer = new int[10 * array_size];
	int* buffer = new int[array_size];
	MPI_Status status;
	fillArray(array);
	double start_time;
	double end_time;
	if (rank == 0) {
		start_time = MPI_Wtime();
		MPI_Send(array, array_size, MPI_INT, 1, 0, MPI_COMM_WORLD);
		end_time = MPI_Wtime();
		printf("Send time = %.6f \n", end_time - start_time);
	}
	else {
		MPI_Recv(buffer, array_size, MPI_INT, 0, 0, MPI_COMM_WORLD, &status);
	}
	if (rank == 0) {
		start_time = MPI_Wtime();
		MPI_Ssend(array, array_size, MPI_INT, 1, 1, MPI_COMM_WORLD);
		end_time = MPI_Wtime();
		printf("Ssend time = %.6f \n", end_time - start_time);
	}
	else {
		MPI_Recv(buffer, array_size, MPI_INT, 0, 1, MPI_COMM_WORLD, &status);
	}
	if (rank == 0) {
		MPI_Buffer_attach(bsend_buffer, 10 * array_size);
		start_time = MPI_Wtime();
		MPI_Bsend(array, array_size, MPI_INT, 1, 2, MPI_COMM_WORLD);
		end_time = MPI_Wtime();
		printf("Bsend time = %.6f \n", end_time - start_time);
	}
	else {
		MPI_Recv(buffer, array_size, MPI_INT, 0, 2, MPI_COMM_WORLD, &status);
	}
	if (rank == 0) {
		start_time = MPI_Wtime();
		MPI_Rsend(array, array_size, MPI_INT, 1, 3, MPI_COMM_WORLD);
		end_time = MPI_Wtime();
		printf("Rsend time = %.6f \n", end_time - start_time);
	}
	else {
		MPI_Recv(buffer, array_size, MPI_INT, 0, 3, MPI_COMM_WORLD, &status);
	}
}

void eleventh_task() {
	MPI_Status st;
	int value;
	if (rank == 0) {
		value = 1;
		MPI_Send(&value, 1, MPI_INT, 1, 0, MPI_COMM_WORLD);
		MPI_Recv(&value, 1, MPI_INT, size - 1, size - 1, MPI_COMM_WORLD, &st);
		printf("value = %d, rank = %d \n", value, rank);
	}
	else {
		int sender = rank - 1;
		int reciver = rank + 1;
		if (rank == size - 1) reciver = 0;
		MPI_Recv(&value, 1, MPI_INT, sender, sender, MPI_COMM_WORLD, &st);
		printf("value = %d, rank = %d \n", value, rank);
		value += rank;
		MPI_Send(&value, 1, MPI_INT, reciver, rank, MPI_COMM_WORLD);
	}
}

int main(int argc, char** argv) {
	/*MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);*/
	twelfth_task();
	MPI_Finalize();
	return EXIT_SUCCESS;
}

