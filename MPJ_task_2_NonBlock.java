import mpi.*;

/** Пересылка сообщения, содержащего ранг текущего процесса, следующему по кольцу процессу.
 * Каждый процесс суммирует свой ранг с принятым значением.
 * Неблокирующий режим пересылки. **/
public class MPJ_task_2_NonBlock {

    public static void main(String[] args) throws Exception {

        // Инициализация распараллеливания
        MPI.Init(args);
        // Ранг процесса
        int rank = MPI.COMM_WORLD.Rank();

        // Число процессов
        int size = MPI.COMM_WORLD.Size();
        int[] buf = new int[1];
        int tag = 0;
        Request[] r = new Request[size];

        if (rank == 0) {
            r[rank] = MPI.COMM_WORLD.Isend(buf, 0, 1, MPI.INT, rank + 1, tag);
            r[rank].Wait();
            MPI.COMM_WORLD.Recv(buf, 0, 1, MPI.INT, MPI.ANY_SOURCE, tag);
            if (r[rank].Is_null()) System.out.println("Отправка сообщения процессу " + (rank + 1) + " успешна");
            System.out.println(buf[0]);
        }
        else {
            MPI.COMM_WORLD.Recv(buf, 0, 1, MPI.INT, MPI.ANY_SOURCE, tag);
            buf[0] += rank;
            r[rank] = MPI.COMM_WORLD.Isend(buf, 0, 1, MPI.INT, (rank + 1) % size, tag);
            r[rank].Wait();
            if (r[rank].Is_null()) System.out.println("Отправка сообщения процессу " + (rank + 1) % size + " успешна");
        }
        MPI.Finalize();
    }
}
