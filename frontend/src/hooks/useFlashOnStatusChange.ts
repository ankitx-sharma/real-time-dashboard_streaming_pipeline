import { useEffect, useRef, useState } from "react";

export function useFlashOnStatusChange<T>(value: T, ms = 250) {
    const prev = useRef<T>(value);
    const [flash, setFlash] = useState(false);

    useEffect(() => {
        if(prev.current != value) {
            setFlash(true);
            const t = setTimeout(() => setFlash(false), ms);
            prev.current = value;
            return () => clearTimeout(t);
        } 
    }, [value, ms]);

    return flash;
}