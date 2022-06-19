#!/usr/bin/python3
import os
import time
import random as rnd


# author: Тыщенко Леонид, 11-902

class Child:
    @staticmethod
    def work(arg: int):
        pid = os.getpid()
        print('Запущена программа Child в процессе с PID ', pid, 'с аргументом', arg)
        time.sleep(arg)
        print('Завершился процесс с PID', pid)
        exit_code = rnd.randint(0, 1)
        os._exit(exit_code)


def main():
    Child.work(rnd.randint(5, 10))


main()
