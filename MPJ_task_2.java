import mpi.*;

/** Пересылка сообщения, содержащего ранг текущего процесса, следующему по кольцу процессу.
 * Каждый процесс суммирует свой ранг с принятым значением.
 * Блокирующий режим пересылки. **/
public class MPJ_task_2 {

    public static void main(String[] args) throws Exception {

        // Инициализация распараллеливания
        MPI.Init(args);
        // Ранг процесса
        int rank = MPI.COMM_WORLD.Rank();

        // Число процессов
        int size = MPI.COMM_WORLD.Size();
        int[] buf = new int[1];
        int tag = 0;

        if (rank == 0) {
            MPI.COMM_WORLD.Send(buf, 0, 1, MPI.INT, rank + 1, tag);
            System.out.println("0 процесс -> 1");
            MPI.COMM_WORLD.Recv(buf, 0, 1, MPI.INT, MPI.ANY_SOURCE, tag);
            System.out.println("Результат: " + buf[0]);
        }
        else {
            MPI.COMM_WORLD.Recv(buf, 0, 1, MPI.INT, MPI.ANY_SOURCE, tag);
            buf[0] += rank;
            MPI.COMM_WORLD.Send(buf, 0, 1, MPI.INT, (rank + 1) % size, tag);
            System.out.println(rank + " процесс -> " + (rank + 1) % size);
        }
        MPI.Finalize();
    }
}
