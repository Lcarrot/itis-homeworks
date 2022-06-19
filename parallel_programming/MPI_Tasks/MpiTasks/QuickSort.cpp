#include <iostream>
#include <cstdlib>
#include <mpi.h>

int rank, size;
const int array_size = 16;

int arraysize(int* array) {
	return _msize(array) / sizeof(int);
}

int getMask(int count, int sec) {
	int result = 0;
	for (int i = 0; i < count; i++) {
		if (i != sec) {
			result += pow(2, sec);
		}
	}
	return result;
}

int calcAverageNumber(int* array) {
	int sum = 0;
	for (int i = 0; i < arraysize(array); i++) {
		sum += array[i];
	}
	return sum / size;
}

int calcSum(int* array) {
	int sum = 0;
	for (int i = 0; i < arraysize(array); i++) {
		sum += array[i];
	}
	return sum;
}

int comparator(const void*, const void*);

int* lessElementPart(int* buffer, int average, int neighbour) {

	MPI_Status status;
	int buffer_size = arraysize(buffer);

	int* less_array = new int[buffer_size];
	int* more_array = new int[buffer_size];
	int less_count = 0;
	int more_count = 0;
	for (int i = 0; i < buffer_size; i++) {
		if (buffer[i] <= average) {
			less_array[less_count] = buffer[i];
			less_count++;
		}
		else {
			more_array[more_count] = buffer[i];
			more_count++;
		}
	}

	MPI_Send(more_array, more_count, MPI_INT, neighbour, 3, MPI_COMM_WORLD);

	int recive_count;
	MPI_Probe(neighbour, 3, MPI_COMM_WORLD, &status);
	MPI_Get_count(&status, MPI_INT, &recive_count);
	int* recive_buffer = new int[recive_count];
	MPI_Recv(recive_buffer, recive_count, MPI_INT, neighbour, 3, MPI_COMM_WORLD, &status);

	buffer_size = less_count + recive_count;
	int* return_array = new int[buffer_size];
	for (int i = 0; i < buffer_size - recive_count; i++) {
		return_array[i] = less_array[i];
	}
	for (int i = 0; i < recive_count; i++) {
		return_array[buffer_size - recive_count + i] = recive_buffer[i];
	}

	delete[] recive_buffer;
	delete[] less_array;
	delete[] more_array;
	delete[] buffer;
	return return_array;
}

int* moreElementPart(int* buffer, int average, int neighbour) {

	int buffer_size = arraysize(buffer);
	MPI_Status status;
	int recive_count;
	MPI_Probe(neighbour, 3, MPI_COMM_WORLD, &status);
	MPI_Get_count(&status, MPI_INT, &recive_count);
	int* recive_buffer = new int[recive_count];
	MPI_Recv(recive_buffer, recive_count, MPI_INT, neighbour, 3, MPI_COMM_WORLD, &status);

	int* less_array = new int[buffer_size];
	int* more_array = new int[buffer_size];
	int less_count = 0;
	int more_count = 0;
	for (int i = 0; i < buffer_size; i++) {
		if (buffer[i] <= average) {
			less_array[less_count] = buffer[i];
			less_count++;
		}
		else {
			more_array[more_count] = buffer[i];
			more_count++;
		}
	}

	MPI_Send(less_array, less_count, MPI_INT, neighbour, 3, MPI_COMM_WORLD);

	buffer_size = more_count + recive_count;
	int* return_array = new int[buffer_size];
	for (int i = 0; i < buffer_size - recive_count; i++) {
		return_array[i] = more_array[i];
	}
	for (int i = 0; i < recive_count; i++) {
		return_array[buffer_size - recive_count + i] = recive_buffer[i];
	}

	delete[] recive_buffer;
	delete[] less_array;
	delete[] more_array;
	delete[] buffer;
	return return_array;
}

int comparator(const void* i, const void* j) {
	return *(int*)i - *(int*)j;
}

//works for 4 threads
void quickSort() {
	int buffer_size = array_size / size;
	int* buffer = new int[array_size / size];
	int* start_array = new int[array_size];
	int average = 0;
	int neighbour = 0;
	if (rank == 0) {
		printf("start array: ");
		for (int i = 0; i < array_size; i++) {
			start_array[i] = rand() % 100;
			printf("%d, ", start_array[i]);
		}
		printf("\n");
	}
	MPI_Scatter(start_array, buffer_size, MPI_INT, buffer, buffer_size, MPI_INT, 0, MPI_COMM_WORLD);
	int sec = log(size) / log(2) - 1;
	while (sec >= 0) {
		if (rank == 0) {
			average = calcAverageNumber(buffer);
		}
		MPI_Bcast(&average, 1, MPI_INT, 0, MPI_COMM_WORLD);
		int neighbour_dist = pow(2, sec);
		if ((rank & neighbour_dist) == 0) {
			neighbour = rank + neighbour_dist;
			buffer = lessElementPart(buffer, average, neighbour);
			buffer_size = arraysize(buffer);
		}
		else {
			neighbour = rank - neighbour_dist;
			buffer = moreElementPart(buffer, average, neighbour);
			buffer_size = arraysize(buffer);
		}
		sec--;
	}
	qsort(buffer, buffer_size, sizeof(int),  comparator);
	if (rank == 0) {
		for (int i = 0; i < buffer_size; i++) {
			start_array[i] = buffer[i];
		}
		MPI_Status status;
		int recive_count;
		for (int i = 1; i < size; i++) {
			MPI_Probe(i, 4, MPI_COMM_WORLD, &status);
			MPI_Get_count(&status, MPI_INT, &recive_count);
			MPI_Recv(&start_array[buffer_size], recive_count, MPI_INT, i, 4, MPI_COMM_WORLD, &status);
			buffer_size += recive_count;
		}
		printf("end array: ");
		for (int i = 0; i < array_size; i++) {
			printf("%d, ", start_array[i]);
		}
		printf("\n");
	}
	else {
		MPI_Send(buffer, buffer_size, MPI_INT, 0, 4, MPI_COMM_WORLD);
	}
}

int main(int argc, char** argv) {
	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &size);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	quickSort();
	MPI_Finalize();
	return EXIT_SUCCESS;
}

