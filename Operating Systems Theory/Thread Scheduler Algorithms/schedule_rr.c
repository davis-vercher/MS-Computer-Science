#include <stdio.h>
#include <stdbool.h>


struct Task {
    char name[10];
    int priority;
    int cpu_burst;
    int remaining_burst;
};

void schedule_rr(struct Task tasks[], int n, int time_quantum) {
    int total_time = 0;

    for (int i = 0; i < n; i++) {
        tasks[i].remaining_burst = tasks[i].cpu_burst;
    }

    while (1) {
        bool done = true;

        for (int i = 0; i < n; i++) {
            if (tasks[i].remaining_burst > 0) {
                done = false;

                if (tasks[i].remaining_burst > time_quantum) {
                    total_time += time_quantum;
                    tasks[i].remaining_burst -= time_quantum;
                } else {
                    total_time += tasks[i].remaining_burst;
                    tasks[i].remaining_burst = 0;
                    printf("Task %s is done.\n", tasks[i].name);
                }
            }
        }

        if (done == true) {
            break;
        }
    }
}
