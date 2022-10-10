import mpi.MPI;
import mpi.Request;

import java.util.Arrays;

public class MPJ_task_3 {
    public static void main(String[] args) throws Exception {

        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        Request[] req = new Request[size];
        int tag = 0;
        // Массив для отправки сообщения
        double[] send_number = new double[1];

        // Нечётные процессы выбирают число и отправляют 1-му процессу
        if (rank % 2 != 0 && rank > 1) {
            send_number[0] = Math.round(Math.random() * 10);
            req[rank] = MPI.COMM_WORLD.Isend(send_number, 0, 1, MPI.DOUBLE, 1, tag);
            req[rank].Wait();
        }
        // Чётные процессы выбирают число и отправляют 2-му процессу
        else if (rank % 2 == 0 && rank > 2) {
            send_number[0] = Math.round(Math.random() * 10);
            req[rank] = MPI.COMM_WORLD.Isend(send_number, 0, 1, MPI.DOUBLE, 2, tag);
            req[rank].Wait();
        }
        // Первый процесс собирает числа в массив, сортирует и отправляет его нулевому процессу
        else if (rank == 1) {
            double[] list_num1 = new double[(size-3)/2];
            for (int i = 0; i < (size-3)/2; i += 1) {
                MPI.COMM_WORLD.Recv(send_number, 0, 1, MPI.DOUBLE, MPI.ANY_SOURCE, tag);
                list_num1[i] = send_number[0];
            }
            Arrays.sort(list_num1);
            System.out.println("Массив 1-го процесса:\n" + Arrays.toString(list_num1));
            MPI.COMM_WORLD.Isend(list_num1, 0, list_num1.length, MPI.DOUBLE, 0, tag);

        }
        // Второй процесс собирает числа в массив, сортирует и отправляет его нулевому процессу
        else if (rank == 2) {
            double[] list_num2 = new double[(size-3)/2];
            for (int i = 0; i < (size-3)/2; i += 1) {
                MPI.COMM_WORLD.Recv(send_number, 0, 1, MPI.DOUBLE, MPI.ANY_SOURCE, tag);
                list_num2[i] = send_number[0];
            }
            Arrays.sort(list_num2);
            System.out.println("Массив 2-го процесса:\n" + Arrays.toString(list_num2));
            MPI.COMM_WORLD.Isend(list_num2, 0, list_num2.length, MPI.DOUBLE, 0, tag);

        }
        // Нулевой процесс получает два массива и объединяет их, сортируя элементы
        else if (rank == 0) {
            double[] list1 = new double[(size-3)/2];
            double[] list2 = new double[(size-3)/2];
            double[] result_list = new double[size-3];
            int position1 = 0;
            int position2 = 0;
            MPI.COMM_WORLD.Recv(list1, 0, list1.length, MPI.DOUBLE, 1, tag);
            MPI.COMM_WORLD.Recv(list2, 0, list2.length, MPI.DOUBLE, 2, tag);
            for (int i = 0; i < result_list.length; i++) {
                if (position1 == list1.length) {
                    result_list[i] = list2[position2];
                    position2++;
                }
                else if (position2 == list2.length) {
                    result_list[i] = list1[position1];
                    position1++;
                }
                else if (list1[position1] < list2[position2]) {
                    result_list[i] = list1[position1];
                    position1++;
                }
                else {
                    result_list[i] = list2[position2];
                    position2++;
                }
            }
            System.out.println("Результат:\n" + Arrays.toString(result_list));
        }
        MPI.Finalize();
    }
}
