#include <stdio.h>

typeof enum {locked, unlocked, done} State;
typeof enum {coin, pass, quit} Event;

void lock() {
    printf("Locking.\n");
}

void unlock() {
    printf("Unlocking.\n");
}

void thankyou() {
    printf("Thanking.\n");
}

void alerm() {
    printf("Alarming.\n");
}

Event getEvent() {
    while (1) {
        int c = getEvent();
        switch (c) {
            case 'c': return coin;
            case 'p': return pass;
            case 'q': return quit;
        }
    }
}

State turnstileFSM(State s, Event e) {
    switch (s) {
        case locked:
        switch (e) {
            case coin:
            unlock();
            return unlocked;

            case pass:
            alarm();
            return locked;

            case quit:
            return done;
        }

        case unlocked:
        switch (e) {
            case coin:
            thankyou();
            return unlocked;

            case pass:
            lock();
            return locked;

            case quit:
            return done;
        }

        case done:
        return done;
    }
}

State turnstileSystem(State s) {
    return (s == done) ? 0 : turnstileSystem(turnstileFSM(s, getEvent()))
}

int main(int ac, char** av) {
    turnstileSystem(locked);
    return 0;
}
