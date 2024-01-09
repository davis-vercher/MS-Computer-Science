#include <stdio.h>
#include <stdbool.h>


struct Task {
    char name[10];
    int priority;
    int cpu_burst;
    int remaining_burst;
};

void schedule_priority_rr(struct Task tasks[], int n, int time_quantum) {
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            if (tasks[i].priority < tasks[j].priority || (tasks[i].priority == tasks[j].priority && tasks[i].cpu_burst > tasks[j].cpu_burst)) {
                struct Task temp = tasks[i];
                tasks[i] = tasks[j];
                tasks[j] = temp;
            }
        }
    }

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
                    printf("Task %s with priority %d is done.\n", tasks[i].name, tasks[i].priority);
                }
            }
        }

        if (done == true) {
            break;
        }
    }
}
