#!/usr/bin/python3
import os
import random as rnd
import time

# author: Тыщенко Леонид, 11-902

class Parent:
    @staticmethod
    def work(fork_count: int):
        processes = []
        for i in range(1, fork_count):
            child_pid = os.fork()
            if child_pid == 0:
                random_number = rnd.randint(5, 10)
                os.execl("child.py", str(random_number))
            else:
                processes.append(child_pid)
        while processes:
            child_pid, exit_code = os.wait()
            if child_pid == 0:
                random_number = rnd.randint(1, 10)
                time.sleep(random_number)
            else:
                print("Дочерний процесс с PID", child_pid, "завершился. Статус завершения.", exit_code)
                processes.remove(child_pid)


def main():
    Parent.work(int(input()))


main()
