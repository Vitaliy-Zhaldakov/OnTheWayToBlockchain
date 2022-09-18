import mpi.*;

/** Применение пробников при отправке сообщений от нулевого и первого процессов ко второму. **/
public class MPJ_task_4 {

    public static void main(String[] args) throws Exception {
        int[] buf1 = new int[1];
        int[] buf2 = {1,3,5};
        int count,TAG = 0;
        Status status;

        buf1[0] = 2016;

        // Инициализация блока распараллеливания
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();

        if (rank == 0) {
            MPI.COMM_WORLD.Send(buf1, 0, 1, MPI.INT, 2, TAG);
        }
        else if (rank == 1) {
            MPI.COMM_WORLD.Send(buf2, 0, buf2.length, MPI.INT, 2, TAG);
        }
        else if (rank == 2) {
            // Пробник сообщения от нулевого процесса
            status = MPI.COMM_WORLD.Probe(0, TAG);
            // Размер принятого сообщения
            count = status.Get_count(MPI.INT);
            int[] back_buf = new int[count];

            MPI.COMM_WORLD.Recv(back_buf,0, count, MPI.INT,0, TAG);
            System.out.print("Rank = 0 Отправил сообщение: ");

            for(int i = 0 ; i < count ; i ++)
                System.out.println(back_buf[i] + " ");

            // Пробник сообщения от первого процесса
            status = MPI.COMM_WORLD.Probe(1, TAG);
            count = status.Get_count(MPI.INT);
            back_buf = new int[count];

            MPI.COMM_WORLD.Recv(back_buf,0, count, MPI.INT,1, TAG);
            System.out.print("Rank = 1 Отправил сообщение: ");

            for(int i = 0 ; i < count ; i ++)
                System.out.print(back_buf[i] + " ");
        }
        MPI.Finalize();
    }
}
