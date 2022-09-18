import mpi.*;

/** Пересылка чётными по рангу процессами сообщения нечётным процессам. **/
public class MPJ_task_1 {

    public static void main(String[] args) throws Exception {

        // Инициализация распараллеливания
        MPI.Init(args);

        // Ранг процесса
        int rank = MPI.COMM_WORLD.Rank();

        // Число процессов
        int size = MPI.COMM_WORLD.Size();
        int[] message = new int[1];
        int tag = 0;

        if (rank % 2 == 0) {
            if (rank + 1 != size)
                message[0] = rank;
                System.out.println("Отправляющий процесс: " + rank);
                // Отправка сообщения следующему по рангу процессу
                MPI.COMM_WORLD.Send(message, 0, 1, MPI.INT, rank + 1, tag);
        }
        else {
                // Принятие сообщения процессом
                MPI.COMM_WORLD.Recv(message, 0, 1, MPI.INT, MPI.ANY_SOURCE, tag);
                System.out.println("Принимающий процесс: " + rank + " Полученное сообщение: " + message[0]);
        }
        MPI.Finalize();
    }
}
