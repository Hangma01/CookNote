import { ref, onMounted, onBeforeUnmount } from 'vue';

export const useTimer = (initialTime = 120, onTimerEnd = () => {}) => {
    const timer = ref(initialTime); // 타이머 시간 (초 단위)
    const timerInterval = ref(null); // setInterval ID
    const isTimerRunning = ref(false); // 타이머 실행 여부

    // 타이머 종료 후 처리
    const handleTimerEnd = () => {
        clearInterval(timerInterval.value); // 타이머 종료
        isTimerRunning.value = false; // 타이머 실행 상태 변경
        onTimerEnd(); // 종료 후 콜백 실행
    };

    // 타이머 시작
    const startTimer = () => {
        if (isTimerRunning.value) return; // 이미 실행 중이면 중복 시작 방지

        isTimerRunning.value = true;
        timerInterval.value = setInterval(() => {
        if (timer.value > 0) {
            timer.value--; // 1초씩 감소
        } else {
            handleTimerEnd(); // 타이머 종료
        }
        }, 1000); // 1초마다 실행
    };

    // 타이머 멈추기
    const stopTimer = () => {
        clearInterval(timerInterval.value);
        isTimerRunning.value = false;
    };

    // 타이머 리셋
    const resetTimer = () => {
        timer.value = initialTime;
        stopTimer();
    };

    // 컴포넌트가 마운트되었을 때 타이머 시작
    onMounted(() => {
        startTimer();
    });

    // 컴포넌트가 언마운트되기 전에 타이머 종료
    onBeforeUnmount(() => {
        stopTimer();
    });

    return {
        timer,
        isTimerRunning,
        startTimer,
        stopTimer,
        resetTimer
    };
};